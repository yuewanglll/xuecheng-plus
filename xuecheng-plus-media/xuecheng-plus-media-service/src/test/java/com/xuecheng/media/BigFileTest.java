package com.xuecheng.media;

import io.minio.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.xuecheng.media.MinioTest.minioClient;

/**
 * @author Mr.M
 * @version 1.0
 * @description 大文件处理测试
 * @date 2022/9/13 9:21
 */
public class BigFileTest {


    //测试文件分块方法
    @Test
    public void testChunk() throws IOException {
        File sourceFile = new File("源文件本地地址");
        String chunkPath = "拆分源文件后保存的地址";
        File chunkFolder = new File(chunkPath);
        if (!chunkFolder.exists()) {
            //如果chunkFolder路径不存在就创建该路径
            chunkFolder.mkdirs();
        }
        //分块大小
        long chunkSize = 1024 * 1024 * 5;
        //分块数量 Math.ceil向上取整
        long chunkNum = (long) Math.ceil(sourceFile.length() * 1.0 / chunkSize);
        System.out.println("分块总数：" + chunkNum);
        //缓冲区大小
        byte[] b = new byte[1024];
        //使用RandomAccessFile访问文件
        RandomAccessFile raf_read = new RandomAccessFile(sourceFile, "r");
        //分块
        for (int i = 0; i < chunkNum; i++) {
            //创建分块文件
            File file = new File(chunkPath + i);
            //如果创建的分块文件已经存在同名的文件则删除
            if (file.exists()) {
                file.delete();
            }
            //创建chunkPath + i这个文件，创建成功返回true
            boolean newFile = file.createNewFile();
            if (newFile) {
                //向分块文件中写数据
                RandomAccessFile raf_write = new RandomAccessFile(file, "rw");
                int len = -1;
                //raf_read.read(b) 将源文件读到缓冲区
                while ((len = raf_read.read(b)) != -1) {
                    //将缓冲区刷新到目标目录
                    raf_write.write(b, 0, len);
                    //超过规定分块的文件大小跳过写下一个分块
                    if (file.length() >= chunkSize) {
                        break;
                    }
                }
                //关流
                raf_write.close();
                System.out.println("完成分块" + i);
            }
        }
        //关流
        raf_read.close();
    }


    //测试文件合并方法
    @Test
    public void testMerge() throws IOException {
        //块文件目录
        File chunkFolder = new File("拆快后的文件目录");
        //原始文件
        File originalFile = new File("原始文件目录");
        //合并文件
        File mergeFile = new File("分块合并后的目录");
        //合并文件的目录如果已经存在则删除
        if (mergeFile.exists()) {
            mergeFile.delete();
        }
        //创建新的合并文件
        mergeFile.createNewFile();
        //用于写文件
        RandomAccessFile raf_write = new RandomAccessFile(mergeFile, "rw");
        //指针指向文件顶端
        raf_write.seek(0);
        //缓冲区
        byte[] b = new byte[1024];
        //获取分块列表所以文件
        File[] fileArray = chunkFolder.listFiles();
        // 转成集合，便于排序
        List<File> fileList = Arrays.asList(fileArray);
        // 从小到大排序
        Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                return Integer.parseInt(o1.getName()) - Integer.parseInt(o2.getName());
            }
        });
        //合并文件
        for (File chunkFile : fileList) {
            RandomAccessFile raf_read = new RandomAccessFile(chunkFile, "rw");
            int len = -1;
            while ((len = raf_read.read(b)) != -1) {
                raf_write.write(b, 0, len);
            }
            raf_read.close();
        }
        raf_write.close();
        //校验文件
        try (
                FileInputStream fileInputStream = new FileInputStream(originalFile);
                FileInputStream mergeFileStream = new FileInputStream(mergeFile);
        ) {
            //取出原始文件的md5
            String originalMd5 = DigestUtils.md5Hex(fileInputStream);
            //取出合并文件的md5进行比较
            String mergeFileMd5 = DigestUtils.md5Hex(mergeFileStream);
            if (originalMd5.equals(mergeFileMd5)) {
                System.out.println("合并文件成功");
            } else {
                System.out.println("合并文件失败");
            }
        }
    }


    //将分块文件上传至minio
    @Test
    public void uploadChunk() {
        String chunkFolderPath = "分块文件所在目录";
        File chunkFolder = new File(chunkFolderPath);
        //获取所有分块文件
        File[] files = chunkFolder.listFiles();
        //将分块文件上传至minio
        for (int i = 0; i < files.length; i++) {
            try {
                UploadObjectArgs uploadObjectArgs = UploadObjectArgs.builder()
                        .bucket("testbucket")
                        .object("chunk/" + i)  //设置上传后的文件路径和名称  eg：文件名chunk/0
                        .filename(files[i].getAbsolutePath())  //getAbsolutePath()获取每个文件或目录的结对路径  上传文件的绝对路径
                        .build();
                minioClient.uploadObject(uploadObjectArgs);
                System.out.println("上传分块成功" + i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    //合并文件，要求分块文件最小5M
    @Test
    public void test_merge() throws Exception {
        List<ComposeSource> sources = Stream.iterate(0, i -> ++i)  //创建Stream的无限流，每次循环i的指递增
                .limit(6)  //无限流的最大值 i=0 1 2 3 4 5
                .map(i -> ComposeSource.builder()   //将i映射到ComposeSource
                        .bucket("testbucket")
                        .object("chunk/".concat(Integer.toString(i))) //要合并文件的目录
                        .build())
                .collect(Collectors.toList());


        ComposeObjectArgs composeObjectArgs = ComposeObjectArgs.builder()
                .bucket("testbucket")
                .object("merge01.mp4")  //文件合并后的路径名称
                .sources(sources)   //需要合并文件快所在的地址
                .build();
        minioClient.composeObject(composeObjectArgs);  //合并文件需要用composeObject

    }

    //清除分块文件
    @Test
    public void test_removeObjects() {
        //合并分块完成将分块文件清除
        List<DeleteObject> deleteObjects = Stream.iterate(0, i -> ++i)
                .limit(6)
                .map(i -> new DeleteObject("chunk/".concat(Integer.toString(i))))  //concat是String类提供的一个api，将一个字符串拼接到另一个字符串末尾
                .collect(Collectors.toList());

        RemoveObjectsArgs removeObjectsArgs = RemoveObjectsArgs.builder().bucket("testbucket").objects(deleteObjects).build();
        Iterable<Result<DeleteError>> results = minioClient.removeObjects(removeObjectsArgs);  //results 是一个可迭代对象，包含了每个删除操作的结果
        results.forEach(r -> {   //遍历results每个删除结果  r表示单独删除操作的结果
            DeleteError deleteError = null;   // deleteError用于存储删除操作的错误信息
            try {
                deleteError = r.get();  //实际上的删除动作 获取删除操作的结果，删除成功deleteError为null，删除失败deleteError将包含错误信息
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
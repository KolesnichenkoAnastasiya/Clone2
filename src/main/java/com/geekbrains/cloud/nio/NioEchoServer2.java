package com.geekbrains.cloud.nio;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class NioEchoServer2 {
    /**
     * Сделать терминал, которые умеет обрабатывать команды:
     * ls - список файлов в директории
     * cd dir_name - переместиться в директорию
     * cat file_name - распечатать содержание файла на экран
     * mkdir dir_name - создать директорию в текущей
     * touch file_name - создать пустой файл в текущей директории
     */
    private ServerSocketChannel serverChannel;
    private Selector selector;
    private ByteBuffer buf;
    private Path currentDir;
    private final String WORKING_DIR = "user.dir";
    String command = " ";
    String param = " ";

    public NioEchoServer2() throws IOException, InterruptedException {
        currentDir = Paths.get(System.getProperty("user.dir"));
        buf = ByteBuffer.allocate(1000);
        serverChannel = ServerSocketChannel.open();
        selector = Selector.open();
        serverChannel.configureBlocking(false);
        serverChannel.bind(new InetSocketAddress(8191));
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("Server started...");
        while (serverChannel.isOpen()) {
            selector.select(); // block
            System.out.println("Keys selected...");
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                if (key.isAcceptable()) {
                    handleAccept();
                }
                if (key.isReadable()) {
                    handleRead(key);
                }
                iterator.remove();
            }
        }
    }
    private void handleRead(SelectionKey key) throws IOException, ArrayIndexOutOfBoundsException, InterruptedException {
        Thread.sleep(2000);
        SocketChannel channel = (SocketChannel) key.channel();
        StringBuilder s = new StringBuilder();
        int read = 0;
        while (true) {
            read = channel.read(buf);
            if (read == 0) {
                break;
            }
            if (read < 0) {
                channel.close();
                return;
            }
            buf.flip();
            while (buf.hasRemaining()) {
                s.append((char) buf.get());
            }
            buf.clear();
        }
        System.out.println("Received: " + s);
        String [] token = new String[2];
        command=s.toString();
        try {
            token = s.toString().split("\\s+", 2);
            command = token[0].trim();
            System.out.println("command: " + token[0]);
            param = token[1].trim();
            System.out.println("param: " + token[1]);}
        catch (Exception e) {
            param =" ";
            e.printStackTrace();}
        try {
            switch (command) {
                case "ls":
                    sendFileList(channel);
                    break;
                case "cd":
                    changeDir(channel, param);
                    break;
                case "cat":
                    readFile(channel, param);
                    break;
                case "mkdir":
                    makeDir(channel, param);
                    break;
                case "touch":
                    makeFile(channel, param);
                    break;
                default:
                    channel.write(ByteBuffer.wrap(s.toString().getBytes(StandardCharsets.UTF_8)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleAccept() throws IOException {
        SocketChannel channel = serverChannel.accept();
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_READ);
        channel.write(ByteBuffer.wrap(
                "Hello user. Welcome to our terminal\n\r".getBytes(StandardCharsets.UTF_8)
        ));
        System.out.println("Client accepted...");
    }
    private void readFile(SocketChannel channel, String param) throws IOException {
        Path filePath = currentDir.resolve(param);
        if (Files.exists(filePath)) {
            if(isFileEmpty(filePath.toFile())==true) {
                channel.write(ByteBuffer.wrap(("File" + param +" file is empty\n\r").getBytes(StandardCharsets.UTF_8)));}
            else {
                try {
                    List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
                    for (String line : lines) {
                        channel.write(ByteBuffer.wrap("\n\r".getBytes(StandardCharsets.UTF_8)));
                        channel.write(ByteBuffer.wrap(line.getBytes(StandardCharsets.UTF_8)));
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }}
        } else {
            channel.write(ByteBuffer.wrap(("File" + param +" not found\n\r").getBytes(StandardCharsets.UTF_8)));
        }}

    public boolean isFileEmpty(File file) {
        return file.length() == 0;
    }

    private void makeFile(SocketChannel channel, String name) throws IOException {
        try {
            Files.createFile(currentDir.resolve(Paths.get(name)));
        } catch (Exception e) {
            e.printStackTrace();
            channel.write(ByteBuffer.wrap(("Failed to create file " + name +
                    " [" + e.getClass().getName() + "]" + "\n\r").getBytes(StandardCharsets.UTF_8)));
        }
    }

    private void makeDir(SocketChannel channel, String name) throws IOException {
        Path newDirectory = Paths.get(name);
        if (!Files.exists(newDirectory)){
            try {
                Files.createDirectory(currentDir.resolve(newDirectory));
                channel.write(ByteBuffer.wrap(("Create directory " + name).getBytes(StandardCharsets.UTF_8)));
            } catch (Exception e) {
                e.printStackTrace();
                channel.write(ByteBuffer.wrap(("Failed to create directory " + name
                        + " [" + e.getClass().getName() + "]" + "\n\r").getBytes(StandardCharsets.UTF_8)));
            }
        }}

    private void changeDir(SocketChannel channel, String param) throws IOException {
        if (currentDir.getFileName().toString().equals(WORKING_DIR) && param.equals("..")) return;
        try {
            Path dir = Paths.get(param);
            if (currentDir.resolve(dir).toFile().exists()) {
                currentDir = currentDir.resolve(dir);
                if (currentDir.getFileName().toString().equals("..")) {
                    currentDir = currentDir.getRoot().resolve(currentDir.subpath(0, currentDir.getNameCount() - 2));
                }
                channel.write(ByteBuffer.wrap(("Current working path is /" + currentDir.getFileName().toString() +
                        "\n\r").getBytes(StandardCharsets.UTF_8)));
                System.out.println(currentDir.toString());
            } else {
                channel.write(ByteBuffer.wrap(("Directory " + param + " doesn`t exist" +
                        "\n\r").getBytes(StandardCharsets.UTF_8)));
            }
        } catch (Exception e) {
            e.printStackTrace();
            channel.write(ByteBuffer.wrap(("Can`t resolve given path. " + param +
                    "\n\r").getBytes(StandardCharsets.UTF_8)));
        }
    }

    private void sendFileList(SocketChannel channel) throws IOException {
        String[] fileArray = currentDir.toFile().list();
        if (fileArray!=null){
            for (String item : fileArray) {
                channel.write(ByteBuffer.wrap((item + "\n\r").getBytes(StandardCharsets.UTF_8)));
            }
        }else {
            channel.write(ByteBuffer.wrap((" Directory is empty\n\r").getBytes(StandardCharsets.UTF_8)));
        }
    }}
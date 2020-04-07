//package FileManager.src.main.java;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;


public class FileManager
{
// （1）实现文件夹创建、删除；
// （2）实现当前文件夹下的内容罗列；
// （3）实现文件拷贝和文件夹拷贝（文件夹拷贝指深度拷贝，包括所有子目录和文件）；
// （4）实现指定文件的加密和解密；
// （5）使用Eclipse、NetBeans或Intellj作为开发环境，使用Maven管理项目，使用Git进行源代码控制；
// （6）在实验报告中，对软件需求进行规范、详细的描述。
    private String currPath;
    public FileManager()
    {
        currPath = System.getProperty("user.dir");
    }
    public Boolean goTo(String path)
    {
        File dir = new File(path);
        if(dir.exists() == false)
        {
            System.out.println("指定的文件夹"+path+"不存在。");
            return false;
        }
        else
        {
            this.currPath = dir.getAbsolutePath();
            System.out.println("已经前往"+this.currPath);
            return true;
        }
    }
    public boolean createDir(String name)
    {
        File dir = new File(this.currPath+File.separator+name);
        if(dir.exists() == true)
        {
            System.out.println("指定的文件夹"+dir.getAbsolutePath()+"已存在。");
            return false;
        }
        else
        {
            boolean res = dir.mkdirs();
            if(res == false)
            {
                System.out.println("文件夹创建失败。");
            }
            return res;
        }
    }
    public boolean deleteDir(String name)
    {
        File dir = new File(this.currPath+File.separator+name);
        if(dir.exists() == false)
        {
            System.out.println("指定的要删除的文件夹不存在。");
            return false;
        }
        else
        {
            if(dir.isDirectory() == true)
                galaxyDel(dir);
            else
                dir.delete();
            return true;
        }
    }

    public void galaxyDel(File f)
    {
        File[] all = f.listFiles();
        if(all == null)
            return;
        else
        {
            for(File temp : all)
            {
                if(temp.isDirectory() == true)
                {
                    galaxyDel(temp);
                }
                else
                {
                    temp.delete();
                }
            }
        }
        f.delete();
    }
    public int listDir()
    {
        File dir = new File(this.currPath);
        System.out.println("当前工作路径为："+this.currPath);
        File[] all = dir.listFiles();
        for(File f : all)
        {
            System.out.println(f.getName());
        }
        return all.length;
    }
    public Boolean fileCopy(String ogName, String destName) throws IOException
    {
        File f = new File(this.currPath+File.separator+ogName);
        FileInputStream fin = new FileInputStream(f);
        FileOutputStream fout = new FileOutputStream(this.currPath+File.separator+destName);
        if(f.exists() == false)
        {
            System.out.println("指定的源文件："+ogName+"不存在。");
            fin.close();
            fout.close();
            return false;
        }
        int n = fin.available() / 10;
        int count = 0;
        byte b[] = new byte[n];
        while ((count = fin.read(b,0,n)) != -1)
        {
            fout.write(b, 0, count);
        }
        fin.close();
        fout.flush();
        fout.close();
        return true;
    }
    private Boolean fileCopy(File og, File dest) throws IOException
    {
        FileInputStream fin = new FileInputStream(og);
        FileOutputStream fout = new FileOutputStream(dest);
        if(og.exists() == false)
        {
            System.out.println("指定的源文件："+og.getAbsolutePath()+"不存在。");
            fin.close();
            fout.close();
            return false;
        }
        int n = fin.available() / 10;
        int count = 0;
        byte b[] = new byte[n];
        while ((count = fin.read(b,0,n)) != -1)
        {
            fout.write(b, 0, count);
        }
        fin.close();
        fout.flush();
        fout.close();
        return true;
    }
    public boolean folderCopy(String ogName, String destPath, String newName) throws IOException
    {
        File f = new File(this.currPath+File.separator+ogName);
        if(f.exists() == false || f.isDirectory() == false)
        {
            System.out.println("指定的源文件夹："+ogName+"不存在。");
            return false;
        }
        File dest = new File(destPath+File.separator+newName);
        dest.mkdir();
        galaxyFold(f, dest);
        return true;
    }
    private void galaxyFold(File og, File dest) throws IOException
    {
        File[] all = og.listFiles();
        if(all == null)
            return;
        else
        {
            for(File temp : all)
            {
                File dest2 = new File(dest, temp.getName());
                if(temp.isDirectory() == true)
                {
                    dest2.mkdir();
                    galaxyFold(temp, dest2);
                }
                else
                {
                    fileCopy(temp, dest2);
                }
            }
        }
    }
    public void encrypt(String ogName, byte pw) throws IOException
    {
        File x = new File(this.currPath+File.separator+ogName);
        if(x.exists() == false)
        {
            System.out.println("请求加密的文件不存在。");
            return;
        }
        FileInputStream fin = new FileInputStream(this.currPath+File.separator+ogName);
        FileOutputStream fout = new FileOutputStream(this.currPath+File.separator+ogName+"_encrypted");
        int n = fin.available() / 10;
        byte buf[] = new byte[n];
        int count = 0;
        while((count = fin.read(buf, 0, n))!=-1)
        {
            for(int i=0; i<count;i++)
            {
                buf[i]=(byte)(buf[i]^pw);
            }
            fout.write(buf, 0, count);
        }
        System.out.println("文件"+ogName+"加密成功");
        fin.close();
        fout.flush();
        fout.close();
    }
    public void decrypt(String ogName, byte pw) throws IOException
    {
        File x = new File(this.currPath+File.separator+ogName);
        if(x.exists() == false)
        {
            System.out.println("请求解密的文件不存在。");
            return;
        }
        FileInputStream fin = new FileInputStream(this.currPath+File.separator+ogName);
        FileOutputStream fout = new FileOutputStream(this.currPath+File.separator+ogName+"_decrypted");
        int n = fin.available() / 10;
        byte buf[] = new byte[n];
        int count = 0;
        while((count = fin.read(buf, 0, n))!=-1)
        {
            for(int i=0; i<count;i++)
            {
                buf[i]=(byte)(buf[i]^pw);
            }
            fout.write(buf, 0, count);
        }
        System.out.println("文件"+ogName+"解密成功");
        fin.close();
        fout.flush();
        fout.close();
    }
    public void wrapper() throws IOException
    {
        while(true)
        {
            Scanner sc = new Scanner(System.in);
            System.out.println("ls - 列目录");
            System.out.println("gt - 前往目录");
            System.out.println("cd - 新建目录");
            System.out.println("dd - 删除目录");
            System.out.println("fc - 拷贝文件");
            System.out.println("dc - 拷贝目录");
            System.out.println("ef - 加密文件");
            System.out.println("df - 解密文件");
            String choice = sc.nextLine();
            switch(choice)
            {
                case "ls":
                    this.listDir();
                    break;
                case "gt":
                    System.out.println("请输入目的路径：");
                    String path = sc.nextLine();
                    this.goTo(path);
                    break;
                case "cd":
                    System.out.println("请输入文件夹名称：");
                    String name = sc.nextLine();
                    this.createDir(name);
                    break;
                case "dd":
                    System.out.println("请输入要删除的文件夹名称：");
                    String delname = sc.nextLine();
                    this.deleteDir(delname);
                    break;
                case "fc":
                    System.out.println("请输入要拷贝文件的名称：");
                    String cname = sc.nextLine();
                    System.out.println("请输入新文件的名称：");
                    String cdname = sc.nextLine();
                    this.fileCopy(cname, cdname);
                    break;
                case "dc":
                    System.out.println("请输入要拷贝文件夹的名称：");
                    String fname = sc.nextLine();
                    System.out.println("请输入要拷贝文件夹的目的地：");
                    String fdpath = sc.nextLine();
                    System.out.println("请输入新文件夹的名称：");
                    String fdname = sc.nextLine();
                    this.folderCopy(fname, fdpath, fdname);
                    break;
                case "ef":
                    System.out.println("请输入要加密文件的名称：");
                    String ename = sc.nextLine();
                    System.out.println("请输入要加密文件的密钥：");
                    byte epw = sc.nextByte();
                    this.encrypt(ename, epw);
                    break;
                case "df":
                    System.out.println("请输入要解密文件的名称：");
                    String dename = sc.nextLine();
                    System.out.println("请输入要解密文件的密钥：");
                    byte pw = sc.nextByte();
                    this.decrypt(dename, (byte)pw);
                    break;
                default:
                    System.out.println("指令不存在，请重新输入：");
                    break;
            }
            //sc.close();
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Hello World!");
        FileManager fm = new FileManager();
        fm.wrapper();
    }
}
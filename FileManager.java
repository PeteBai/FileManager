import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


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
            System.out.println("指定的文件夹不存在。");
            return false;
        }
        else
        {
            boolean res = dir.delete();
            if(res == false)
            {
                System.out.println("文件夹删除失败。");
            }
            return res;
        }
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
    public void encrypt(String ogName) throws IOException
    {
        byte pw = 6;
        File x = new File(ogName);
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
    }
    public void decrypt(String ogName, byte pw) throws IOException
    {
        File x = new File(ogName);
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
    }


    public static void main(String[] args) throws IOException {
        System.out.println("Hello World!");
        FileManager fm = new FileManager();
        //fm.goTo("C:\\Users\\surface\\Desktop");
        //fm.createDir("koko");
        //fm.fileCopy("og.txt", "dest.txt");
        //fm.folderCopy("kk", "C:\\Users\\surface\\Desktop", "kjk");
        fm.decrypt("dest.txt_encryped", (byte)6);
    }
}
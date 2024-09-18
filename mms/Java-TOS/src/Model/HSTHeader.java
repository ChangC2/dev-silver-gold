package Model;

public class HSTHeader {

    //https://stackoverflow.com/questions/2151960/a-c-structure-accessed-in-java
    //https://mkyong.com/java/how-to-read-and-write-java-object-to-a-file/
    //https://www.tabnine.com/code/java/methods/java.io.DataInputStream/readDouble

    public short version = 3;
    public short dataOffset = 0;
    public short itemLength = 0;
    public short nameFieldLength = 0;
    public byte[] otherHeads = new byte[60];
    public short numsOfVar = 0;
}

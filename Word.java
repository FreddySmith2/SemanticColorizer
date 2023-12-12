
/**
 * Word Object
 *
 * @author Freddy Smith
 */
public class Word
{
    String word;
    int vec;
    int hex;
    
    /**
     * Constructor for objects of class Word
     */
    public Word(String word, int vec, int hex)
    {
        this.word = word;
        this.vec = vec;
        this.hex = hex;
    }
    
    public String getColor(){
        return ""+hex;
    }
    
    public String getWord(){
        return word;
    }
    
    public int getVec(){
        return vec;
    }
}

import java.io.FileReader; 
import java.util.Iterator; 
import java.util.Map; 
import java.util.HashMap;
import java.util.ArrayList;
import java.util.*;
  
import org.json.simple.JSONArray; 
import org.json.simple.JSONObject; 
import org.json.simple.parser.*;

public class DictionarySearch{
    public Map<String, Long> words;
    
    public ArrayList<Long> sortedValues;
    
    public int lightMod;
    public int satMod;
    public int hueShift;
    
    public DictionarySearch() throws ParseException, java.io.IOException {
        Object obj = new JSONParser().parse(new FileReader("word_list.json"));
        
        JSONArray jsonArray = (JSONArray) obj;
        
        words = new HashMap<String, Long>();
        
        Iterator itr = jsonArray.iterator();
        
        while(itr.hasNext()){
            String key;
            Long value;
            JSONArray pair = (JSONArray) itr.next();
            Iterator itr2 = pair.iterator();
            key = (String) itr2.next();
            value = (Long) itr2.next();
            words.put(key, value);
        }
        
        System.out.println(words.size());
        System.out.println(findLargestValue()+"");
        //vowelsInARow();
        vowelToConsRatio();
        sortedValues = wordSortByValue();
        hueShift = 0;
        lightMod = 100;
        satMod = 100;
    }
    
    public ArrayList<Long> wordSortByValue(){
        sortedValues = new ArrayList<Long>();
        for(Map.Entry i : words.entrySet()){
            sortedValues.add((Long)i.getValue());
        }
        Collections.sort(sortedValues);
        return sortedValues;
    }
    
    public Long getValueFromKey(String key){
        return words.get(key);
    }
    
    public String getHexFromKey(String key){
        return intToHex(compressor(words.get(key)));
    }
    
    public String getHSLFromKey(String key){
        key = key.toLowerCase();
        return intToHSL(compressor(words.get(key)), words.get(key));
    }
    
    public int getIndexOfSortedValue(Long value){
        int i;
        for(i = 0; i<sortedValues.size(); i++){
            if (sortedValues.get(i)==value){
                return i;
            }
        }
        return i;
    }
    
    public void setHueShift(Double value){
        hueShift = value.intValue();
    }
    
    public void setLightMod(Double value){
        lightMod = value.intValue();
    }
    
    public void setSatMod(Double value){
        satMod = value.intValue();
    }
    
    public String intToHSL(int value, Long inVal){
        String output = "";
        
        
        
        //double s = (double)100-((double) getIndexOfSortedValue(inVal) / ((double)((double) sortedValues.size())/100));
        double s = Math.pow((double)inVal, 1.0 / satMod);
        //double l = (double)100-((double) getIndexOfSortedValue(inVal) / ((double)((double) sortedValues.size())/100));
        double l = Math.pow((double)inVal, 1.0 / lightMod);
        double p = Math.abs(s-50)/2;
        double q = Math.abs(l-50)/2;
        if(s>50){
            //s=s-p;
        }else{
            s=s+p+(p/2);
        }
        
        if(l>50){
            //l=l-q;
        }else{
            l=l+q+(q/2);
        }
        
        //int counter = 
        double a = 180;//graph adjusters
        double b = 2.8;
        value = 16777216-value;
        double o = 881;
        //double x = a*Math.pow(-Math.log(1-(double) value / 16777216), 1 / b);
        //double x = (double) getIndexOfSortedValue(inVal) / ((double)((double) sortedValues.size())/360);
        
        //double x = Math.sqrt((double)inVal / o); // 
        double x = Math.pow((double)inVal, 1.0 / 2.9);
        
        if(x>360){
            x=360;
        }
        x=x+hueShift;
        if(x>360){
            x=x-360;
        }
        int h = (int) x;
        
        int sO = satMod-(int)s;
        int lO = lightMod-(int)l;
        if(sO>100){
            sO=100;
        }
        if(lO>100){
            lO=100;
        }
        if(sO<0){
            sO=0;
        }
        if(lO<0){
            lO=0;
        }
        if(sO>100||sO<0||lO>100||lO<0){
            throw new RuntimeException();
        }
        output = h+", "+sO+"%, "+lO+"%";
        //151983633
        //range of 270 hues
        //int s and l higher at lower numbers etc
        return "hsl("+output+")";
    }
    
    public int compressor(Long value){
        double out = (double) value/9.05893045;
        out = (16777216-out)+1;
        //value = value/10;
        //value = (16777216-value)-1578853;
        return (int)out;
    }
    
    public String intToHex(int value){
        String output = "";
        int temp;
        char[] hexa = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};  
        while(value > 0){
            temp = value % 16;
            output=hexa[temp]+output;
            value=value/16;
        }
        int x = output.length();
        if(output.length()<6){
            for(int i = 0; i < 6-x; i++){
                output="0"+output;
                //output = output.length() < 6 ? "0".repeat(6-output.length()) + output : output;
            }
        }else if(output.length()>6){
            output="000000";
        }
        output = "#"+output;
        return output;
    }
    
    public boolean isVowel(char ch){
        if(ch=='a' || ch=='e' || ch=='i' || ch=='o' || ch=='u' || ch=='y'){
            return true;
        }
        return false;
    }
    
    public boolean isCons(char ch){
        if(ch=='a' || ch=='e' || ch=='i' || ch=='o' || ch=='u'){
            return false;
        }
        return true;
    }
    
    public void vowelToConsRatio(){
        String temp;
        int vows = 0;
        int cons = 0;
        int tempo = 0;
        for(Map.Entry i : words.entrySet()){
            temp = (String) i.getKey();
            for(int j = 0; j<temp.length(); j++){
                if(isVowel(temp.charAt(j))){
                    vows++;
                }else{
                    cons++;
                }
            }
            tempo = Math.abs(vows-cons);
            if(tempo>6){
                System.out.println(temp);
            }
            tempo = 0;
            vows = 0;
            cons = 0;
        }
    }
    
    public void vowelsInARow(){
        String temp;
        int count = 0;
        boolean track = false;
        //System.out.println("debug 1");
        for(Map.Entry i : words.entrySet()){
            temp = (String) i.getKey();
            //System.out.println("debug 2");
            for(int j = 0; j<temp.length(); j++){
                //System.out.println("debug 3");
                if(!track){
                    //System.out.println("debug 4");
                    if(isVowel(temp.charAt(j))){
                        track=true;
                        //System.out.println("debug 5");
                        if(count<4){
                            //System.out.println("debug 6");
                            count = 1;
                        }
                    }
                    else{
                        track=false;
                    }
                }else{
                    //System.out.println("debug 7");
                    if(isVowel(temp.charAt(j))){
                        track=true;
                        //System.out.println("debug 8");
                        count++;
                    }else{
                        //System.out.println("debug 9");
                        track=false;
                        if(count<4){
                            //System.out.println("debug 10");
                            count=0;
                        }
                    }
                }
                //System.out.println("debug 11");
                temp.charAt(j);
                
            }
            if(count>3){
                //System.out.println("debug 12");
                System.out.println(temp);
            }
            //System.out.println("debug 13");
            track=false;
            count = 0;
        }
    }
    
    public void lemmeSee(){
        ArrayList<Map.Entry> wordList = new ArrayList<Map.Entry>();
        for(Map.Entry i : words.entrySet()){
            wordList.add(i);
        }
        
    }
    
    public void printer(String word){
        Long vector;
        vector = words.get(word);
        vector = vector/10;
        
    }
    
    
    
    public Long findLargestValue(){
        Long output = 0L;
        for(Map.Entry i : words.entrySet()){
            Long temp = (Long)i.getValue();
            if(temp>output){
                output=temp;
            }
        }
        return output;
    }
}
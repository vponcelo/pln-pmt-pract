
import edu.stanford.nlp.ling.TaggedWord;
import java.util.*;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Matias
 */
public class SPRLFeatures {
    private Map<String, Object> f2pos;
    private Map<String, Object> f2neg;
    public SPRLFeatures() {
        f2pos = new HashMap<String, Object>();
        f2neg = new HashMap<String, Object>();
    }
    
    public Map<String, Object> getF2pos() {
        return f2pos;
    }
    public Map<String, Object> getF2neg() {
        return f2neg;
    }
    
    void findF2pos(int k,
                List<String> sentenceSpatialIndicators,
                List<Integer> sentenceSpatialIndicatorsIndex,
                List<Integer> sentenceSpatialIndicatorsWordCount,
                List<String> listHeadWordsPOS,
                Stemmer stemmer, 
                ArrayList<TaggedWord> tSentence
               ) {
             
        f2pos.put("SPATIAL_INDICATOR", sentenceSpatialIndicators.get(k));
        f2pos.put("SPATIAL_INDICATOR_LEMMA", stemmer.stem(sentenceSpatialIndicators.get(k)));
        f2pos.put("SPATIAL_INDICATOR_POS", tSentence.get(sentenceSpatialIndicatorsIndex.get(k)).tag());
        for(int idx = sentenceSpatialIndicatorsIndex.get(k)-1; idx >=0; idx--){
            //HEAD1 features, searches to the left part
            if(listHeadWordsPOS.contains(tSentence.get(idx).tag())){
                f2pos.put("HEAD1", tSentence.get(idx).word());
                f2pos.put("HEAD1_POS", tSentence.get(idx).tag());                                                        
                f2pos.put("HEAD1_LEMMA", stemmer.stem(tSentence.get(idx).word()));
                break;
            }
        }
        for(int idx = sentenceSpatialIndicatorsIndex.get(k)+sentenceSpatialIndicatorsWordCount.get(k); idx < tSentence.size(); idx++){
            //HEAD2 features, searches to the right part
            if(listHeadWordsPOS.contains(tSentence.get(idx).tag())){
                f2pos.put("HEAD2", tSentence.get(idx).word());
                f2pos.put("HEAD2_POS", tSentence.get(idx).tag());                            
                f2pos.put("HEAD2_LEMMA", stemmer.stem(tSentence.get(idx).word()));
                break;
            }
        }        
    }

    void findF2neg(int k,
                   int idx,
                   List<List<String>> possibleSpatialIndicators,
                   List<String> listHeadWordsPOS,
                   Stemmer stemmer,
                   ArrayList<TaggedWord> tSentence
                  ) {
              
        f2neg.put("SPATIAL_INDICATOR", join(possibleSpatialIndicators.get(k)," "));
        f2neg.put("SPATIAL_INDICATOR_LEMMA", stemmer.stem(join(possibleSpatialIndicators.get(k)," ")));
        f2neg.put("SPATIAL_INDICATOR_POS", tSentence.get(idx).tag());
        for(int idxx = idx-1; idxx >=0; idxx--){
            if(listHeadWordsPOS.contains(tSentence.get(idxx).tag())){
                f2neg.put("HEAD1", tSentence.get(idxx).word());
                f2neg.put("HEAD1_POS", tSentence.get(idxx).tag());                                                        
                f2neg.put("HEAD1_LEMMA", stemmer.stem(tSentence.get(idxx).word()));
                break;
            }
        }
        for(int idxx = idx+possibleSpatialIndicators.get(k).size(); idxx < tSentence.size(); idxx++){
            if(listHeadWordsPOS.contains(tSentence.get(idxx).tag())){
                f2neg.put("HEAD2", tSentence.get(idxx).word());
                f2neg.put("HEAD2_POS", tSentence.get(idxx).tag());                            
                f2neg.put("HEAD2_LEMMA", stemmer.stem(tSentence.get(idxx).word()));
                break;
            }
        }
    }
    
    public static String join(Collection s, String delimiter) {
        StringBuffer buffer = new StringBuffer();
        Iterator iter = s.iterator();
        while (iter.hasNext()) {
            buffer.append(iter.next());
            if (iter.hasNext()) {
                buffer.append(delimiter);
            }
        }
        return buffer.toString();
    }
}

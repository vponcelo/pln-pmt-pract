/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Bogdan
 */

import com.sun.org.apache.xerces.internal.jaxp.SAXParserImpl;
import java.io.*;
import java.util.*;

import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import org.apache.xerces.parsers.DOMParser;


        
     




public class Main {
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
    public static void main(String args[]){
        // the stemmer
        Stemmer stemmer = new Stemmer();
        
        // the set of all possible spatial indicators
        Set<String> spatialIndicators = new TreeSet<String>();
        
        // The classifier
        String[] classes = {"SI", "NSI"};
        String[] attributes = {"SPATIAL_INDICATOR", "SPATIAL_INDICATOR_POS", "SPATIAL_INDICATOR_LEMMA", "HEAD1", "HEAD2", "HEAD1_POS", "HEAD2_POS", "HEAD1_LEMMA", "HEAD2_LEMMA"};
        NaiveBayes classifier = new NaiveBayes(classes, attributes);
        
        // POS tags of headwords
        String[] headWordsPOS = {"NN", "NNS", "NNP", "NNPS", "PRP", "PRP$", "WP$", "CD"};
        List<String> listHeadWordsPOS = Arrays.asList(headWordsPOS);
        
        // List of all possible spatial indicators
        List<List<String>> possibleSpatialIndicators = new ArrayList<List<String>>();
        Scanner scan;
        try{
            scan = new Scanner(new File( "spatial_indicators.txt" ));
            scan.useDelimiter(",");
            while(scan.hasNext()){
                String chunk = scan.next();                
                possibleSpatialIndicators.add(Arrays.asList(chunk.split(" ")));
            }
        }catch(Exception e){
            
        }
        System.out.println(possibleSpatialIndicators);
        
        try{
            MaxentTagger tagger = new MaxentTagger("left3words-wsj-0-18.tagger");           
            DOMParser parser = new DOMParser();
            parser.parse("sprl_semeval3_trial0.xml");
            Document doc = parser.getDocument();
            NodeList adnotatedSentences = doc.getElementsByTagName("SENTENCE");
            
            System.out.println(adnotatedSentences.getLength());
            
            for(int i = 0; i < adnotatedSentences.getLength(); i++){
                
                Node adnotetedSentence = adnotatedSentences.item(i);
                NodeList details = adnotetedSentence.getChildNodes();
                
                String sentenceContent = "";
                List<String> sentenceSpatialIndicators = new ArrayList<String>();
                List<Integer> sentenceSpatialIndicatorsIndex = new ArrayList<Integer>();
                List<Integer> sentenceSpatialIndicatorsWordCount = new ArrayList<Integer>();
                
                for(int j = 0; j < details.getLength(); j++){
                    Node detail = details.item(j);
                    if(detail.getNodeName().equals("CONTENT")){
                        sentenceContent = detail.getChildNodes().item(0).getNodeValue();                        
                    } else if(detail.getNodeName().equals("SPATIAL_INDICATOR")){
                        String si = detail.getChildNodes().item(0).getNodeValue().trim();
                        sentenceSpatialIndicators.add(si);
                        spatialIndicators.add(si);
                        sentenceSpatialIndicatorsWordCount.add(si.split(" ").length);
                        String id = detail.getAttributes().getNamedItem("id").getNodeValue();
                        sentenceSpatialIndicatorsIndex.add(Integer.parseInt(id.substring(2)));                        
                    }
                }
                /*
                System.out.println("CONTENT= \""+sentenceContent+"\"");
                System.out.println("SPATIAL_INDICATOR= \""+sentenceSpatialIndicators+"\"");
                System.out.println("SPATIAL_INDICATOR_INDEX= \""+sentenceSpatialIndicatorsIndex+"\"");
                System.out.println("SPATIAL_INDICATOR_WORD_COUNT= \""+sentenceSpatialIndicatorsWordCount+"\"");
                */
                List<List<HasWord>> sentences = tagger.tokenizeText(new StringReader(sentenceContent));
                ArrayList<TaggedWord> tSentence = tagger.tagSentence(sentences.get(0));
                /*for (List<HasWord> sentence : sentences) {
                    System.out.println("SENTENCE: "+sentence);
                    ArrayList<TaggedWord> tSentence = tagger.tagSentence(sentence);
                    System.out.println(Sentence.listToString(tSentence, false));
                }*/
                
                for(int k = 0; k < sentenceSpatialIndicators.size(); k++){
                    Map<String, Object> featureVector = new HashMap<String, Object>();
                    featureVector.put("SPATIAL_INDICATOR", sentenceSpatialIndicators.get(k));
                    featureVector.put("SPATIAL_INDICATOR_LEMMA", stemmer.stem(sentenceSpatialIndicators.get(k)));
                    featureVector.put("SPATIAL_INDICATOR_POS", tSentence.get(sentenceSpatialIndicatorsIndex.get(k)).tag());
                    for(int idx = sentenceSpatialIndicatorsIndex.get(k)-1; idx >=0; idx--){
                        if(listHeadWordsPOS.contains(tSentence.get(idx).tag())){
                            featureVector.put("HEAD1", tSentence.get(idx).word());
                            featureVector.put("HEAD1_POS", tSentence.get(idx).tag());                                                        
                            featureVector.put("HEAD1_LEMMA", stemmer.stem(tSentence.get(idx).word()));
                            break;
                        }
                    }
                    for(int idx = sentenceSpatialIndicatorsIndex.get(k)+sentenceSpatialIndicatorsWordCount.get(k); idx < tSentence.size(); idx++){
                        if(listHeadWordsPOS.contains(tSentence.get(idx).tag())){
                            featureVector.put("HEAD2", tSentence.get(idx).word());
                            featureVector.put("HEAD2_POS", tSentence.get(idx).tag());                            
                            featureVector.put("HEAD2_LEMMA", stemmer.stem(tSentence.get(idx).word()));
                            break;
                        }
                    }
                    
                    // Get the negative examples                    
                    
                    System.out.println("VECTOR(+) = " + featureVector);
                    classifier.learn(featureVector, "SI");
                }                
                for(int k = 0; k < possibleSpatialIndicators.size(); k++){
                    for(int idx = 0; idx < tSentence.size() - possibleSpatialIndicators.get(k).size(); idx++){
                        if(!sentenceSpatialIndicatorsIndex.contains(idx)){
                            boolean match = true;
                            for(int iter = 0; iter < possibleSpatialIndicators.get(k).size(); iter++){
                                if(!tSentence.get(idx+iter).word().equals(possibleSpatialIndicators.get(k).get(iter))){
                                    match = false;
                                }
                            }
                            if(match){                                
                                
                                Map<String, Object> featureVector = new HashMap<String, Object>();
                                featureVector.put("SPATIAL_INDICATOR", join(possibleSpatialIndicators.get(k)," "));
                                featureVector.put("SPATIAL_INDICATOR_LEMMA", stemmer.stem(join(possibleSpatialIndicators.get(k)," ")));
                                featureVector.put("SPATIAL_INDICATOR_POS", tSentence.get(idx).tag());
                                for(int idxx = idx-1; idxx >=0; idxx--){
                                    if(listHeadWordsPOS.contains(tSentence.get(idxx).tag())){
                                        featureVector.put("HEAD1", tSentence.get(idxx).word());
                                        featureVector.put("HEAD1_POS", tSentence.get(idxx).tag());                                                        
                                        featureVector.put("HEAD1_LEMMA", stemmer.stem(tSentence.get(idxx).word()));
                                        break;
                                    }
                                }
                                for(int idxx = idx+possibleSpatialIndicators.get(k).size(); idxx < tSentence.size(); idxx++){
                                    if(listHeadWordsPOS.contains(tSentence.get(idxx).tag())){
                                        featureVector.put("HEAD2", tSentence.get(idxx).word());
                                        featureVector.put("HEAD2_POS", tSentence.get(idxx).tag());                            
                                        featureVector.put("HEAD2_LEMMA", stemmer.stem(tSentence.get(idxx).word()));
                                        break;
                                    }
                                }
                                System.out.println("VECTOR(-) = " + featureVector);
                                classifier.learn(featureVector, "NSI");                              
                            }
                        }
                    }
                }                
            }
            
            
        } catch(Exception e){
            
        }
        Map<String, Object> ni1 = new TreeMap<String, Object>();
        //{HEAD1=glass, HEAD1_LEMMA=glass, HEAD1_POS=NN, HEAD2=extent, HEAD2_LEMMA=extent, HEAD2_POS=NN, SPATIAL_INDICATOR=to, SPATIAL_INDICATOR_LEMMA=to, SPATIAL_INDICATOR_POS=TO}
        ni1.put("HEAD1", "glass");
        ni1.put("HEAD1_LEMMA", "glass");
        ni1.put("HEAD1_POS", "NN");
        ni1.put("HEAD2", "extent");
        ni1.put("HEAD2_LEMMA", "extent");
        ni1.put("HEAD2_POS", "NN");
        ni1.put("SPATIAL_INDICATOR", "to");
        ni1.put("SPATIAL_INDICATOR_LEMMA", "to");
        ni1.put("SPATIAL_INDICATOR_POS", "TO");
        System.out.println("CLASS= "+   classifier.classify(ni1));
        Map<String, Object> ni2 = new TreeMap<String, Object>();
        //{HEAD1_POS=NN, SPATIAL_INDICATOR_LEMMA=in, HEAD2_LEMMA=foreground, HEAD2=foreground, 
        //HEAD1=pool, SPATIAL_INDICATOR=in, SPATIAL_INDICATOR_POS=IN, HEAD2_POS=NN, HEAD1_LEMMA=pool}
        ni2.put("HEAD1", "pool");
        ni2.put("HEAD1_LEMMA", "pool");
        ni2.put("HEAD1_POS", "NN");
        ni2.put("HEAD2", "foreground");
        ni2.put("HEAD2_LEMMA", "foreground");
        ni2.put("HEAD2_POS", "NN");
        ni2.put("SPATIAL_INDICATOR", "in");
        ni2.put("SPATIAL_INDICATOR_LEMMA", "in");
        ni2.put("SPATIAL_INDICATOR_POS", "IN");
        System.out.println("CLASS= "+   classifier.classify(ni2));
        //System.out.println("ALL SPATIAL INDICATORS: "+spatialIndicators);
    }
}

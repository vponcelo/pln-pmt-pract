
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.trees.*;
import java.io.File;
import java.io.StringReader;
import java.util.*;
import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Matias
 */
public class Main {
    
    public static boolean TRACE = false;
    private static int NSENTENCES = 599;
    
    public static void main(String args[]){
        
        // the stemmer
        Stemmer stemmer = new Stemmer();

        // the parser
        LexicalizedParser lp = new LexicalizedParser("grammar/englishPCFG.ser.gz");
                        
        // the set of all possible spatial indicators
        Set<String> spatialIndicators = new TreeSet<String>();
        
        // SI Classifier
        String[] classes = {"SI", "NSI"};
        String[] attributes = {"HEAD1", "HEAD1LEMMA", "HEAD1POS", "HEAD2", "HEAD2LEMMA", "HEAD2POS", "PREP", "PREPPOS", "PREPSPATIAL"};
        NaiveBayes classifier = new NaiveBayes(classes, attributes);
        
        // MULTICLASS Classifier
        String[] classesMulti = {"T","L","N"};
        //TODO: Expand attributes
        String[] attributesMulti = {"SPATIAL_INDICATOR", "SPATIAL_INDICATOR_POS", "SPATIAL_INDICATOR_LEMMA", "HEAD1", "HEAD2", "HEAD1_POS", "HEAD2_POS", "HEAD1_LEMMA", "HEAD2_LEMMA", "WORD_FORM", "WORD_POS"};
        NaiveBayes classifierM = new NaiveBayes(classesMulti, attributesMulti);        
        
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
        System.out.println( "Possible SI: " + possibleSpatialIndicators);
        
        try{
            //Parse the SemEval sentences
            MaxentTagger tagger = new MaxentTagger("left3words-wsj-0-18.tagger");           
            DOMParser parser = new DOMParser();
            parser.parse("SItrain.xml");
            Document doc = parser.getDocument();
            NodeList trainingSentences = doc.getElementsByTagName("SENTENCE");
            System.out.println(NSENTENCES + " sentences");
            
            //Maximum is -- anotatedSentences.getLength() --
            for(int i = 0; i < NSENTENCES; i++){
                
                Node annotatedSentence = trainingSentences.item(i);
                NodeList details = annotatedSentence.getChildNodes();
                NodeList features = null;
                
                String sentenceContent = "";
                List<String> sentenceSpatialIndicators = new ArrayList<String>();
                List<Integer> sentenceSpatialIndicatorsIndex = new ArrayList<Integer>();
                List<Integer> sentenceSpatialIndicatorsWordCount = new ArrayList<Integer>();
                
                //Extracts the sentence information
                for(int j = 0; j < details.getLength(); j++){
                    Node detail = details.item(j);
                    // -- CONTENT --
                    if(detail.getNodeName().equals("CONTENT")){
                        sentenceContent = detail.getChildNodes().item(0).getNodeValue();                        
                    // TODO: Missing -- TRAJECTOR --
                    // TODO: Missing -- LANDMARK --
                    // -- SPATIAL INDICATOR --
                    } else if(detail.getNodeName().equals("SPATIAL_INDICATOR")){
                        String si = detail.getChildNodes().item(0).getNodeValue().trim();
                        //Add to the sentence SI list
                        sentenceSpatialIndicators.add(si);
                        //Add to the global SI list
                        spatialIndicators.add(si);
                        //sentenceSpatialIndicatorsWordCount: number of words for each SI
                        sentenceSpatialIndicatorsWordCount.add(si.split(" ").length);
                        String id = detail.getAttributes().getNamedItem("id").getNodeValue();
                        //sentenceSpatialIndicatorsIndex: position of SI in the sentence
                        sentenceSpatialIndicatorsIndex.add(Integer.parseInt(id.substring(2)));     
                    // TODO: Missing -- RELATION --
                    // -- PREPS --
                    } else if(detail.getNodeName().equals("PREPS")){
                        features = detail.getChildNodes();
                    }                       
                }                          
                
                for(int p = 0; p < features.getLength(); p++){
                    if(features.item(p).getNodeName().equals("PREPOSITION")) {
                        NodeList prepItems = features.item(p).getChildNodes();
                        SPRLFeatures2 sprl = new SPRLFeatures2();
                        sprl.putF2("HEAD1", prepItems.item(0).getNodeValue());
                        sprl.putF2("HEAD1LEMMA", prepItems.item(1).getNodeValue());
                        sprl.putF2("HEAD1POS", prepItems.item(2).getNodeValue());
                        sprl.putF2("HEAD2", prepItems.item(3).getNodeValue());
                        sprl.putF2("HEAD2LEMMA", prepItems.item(4).getNodeValue());
                        sprl.putF2("HEAD2POS", prepItems.item(5).getNodeValue());
                        sprl.putF2("PREP", prepItems.item(6).getNodeValue());
                        sprl.putF2("PREPPOS", prepItems.item(7).getNodeValue());
                        sprl.putF2("PREPSPATIAL", prepItems.item(8).getNodeValue());
                        String prepclass = prepItems.item(8).getNodeValue();
                        if(prepclass.equals("SI")) {
                            //Positive instance
                            System.out.println("Positive instance: " + sprl.getF2());
                            classifier.learn(sprl.getF2(), "SI");
                        }else if(prepclass.equals("NSI")) {
                            //Negative instance
                            System.out.println("Negative instance: " + sprl.getF2());
                            classifier.learn(sprl.getF2(), "NSI");
                        }
                    }
                }
                
            }
            
            
        } catch(Exception e){
            System.out.println(e);
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

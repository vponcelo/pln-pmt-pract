/****************************************
* PLN-PMT : Spatial Role Labeling Task  *
****************************************/

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

public class Main {
    
    public static boolean TRACE = true;
    
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
        
        // the parser
        LexicalizedParser lp = new LexicalizedParser("grammar/englishPCFG.ser.gz");
                        
        // the set of all possible spatial indicators
        Set<String> spatialIndicators = new TreeSet<String>();
        
        // SI Classifier
        String[] classes = {"SI", "NSI"};
        String[] attributes = {"SPATIAL_INDICATOR", "SPATIAL_INDICATOR_POS", "SPATIAL_INDICATOR_LEMMA", "HEAD1", "HEAD2", "HEAD1_POS", "HEAD2_POS", "HEAD1_LEMMA", "HEAD2_LEMMA"};
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
            parser.parse("sprl_semeval3_trial0.xml");
            Document doc = parser.getDocument();
            NodeList anotatedSentences = doc.getElementsByTagName("SENTENCE");
            
            System.out.println(anotatedSentences.getLength() + " sentences");
            
            for(int i = 0; i < anotatedSentences.getLength(); i++){
                
                Node annotatedSentence = anotatedSentences.item(i);
                NodeList details = annotatedSentence.getChildNodes();
                
                String sentenceContent = "";
                List<String> sentenceSpatialIndicators = new ArrayList<String>();
                List<Integer> sentenceSpatialIndicatorsIndex = new ArrayList<Integer>();
                List<Integer> sentenceSpatialIndicatorsWordCount = new ArrayList<Integer>();
                
                //Extracts the sentence information
                for(int j = 0; j < details.getLength(); j++){
                    Node detail = details.item(j);
                    //Obtains the content of the sentence
                    if(detail.getNodeName().equals("CONTENT")){
                        sentenceContent = detail.getChildNodes().item(0).getNodeValue();                        
                    //Obtains the spatial indicators of the sentence
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
                    }
                }
                
                /*
                System.out.println("CONTENT= \""+sentenceContent+"\"");
                System.out.println("SPATIAL_INDICATOR= \""+sentenceSpatialIndicators+"\"");
                System.out.println("SPATIAL_INDICATOR_INDEX= \""+sentenceSpatialIndicatorsIndex+"\"");
                System.out.println("SPATIAL_INDICATOR_WORD_COUNT= \""+sentenceSpatialIndicatorsWordCount+"\"");
                */
                
                //Tokenize the sentence (tSentence)
                List<List<HasWord>> sentences = MaxentTagger.tokenizeText(new StringReader(sentenceContent));
                ArrayList<TaggedWord> tSentence = tagger.tagSentence(sentences.get(0));
                
                //Parse sentence              
                // This option shows parsing a list of correctly tokenized words
                Tree sentenceTree = lp.apply(sentenceContent); 
                if(TRACE) {
                    sentenceTree.pennPrint();
                    System.out.println();
                    System.out.println(i + ": " + sentenceContent);
                }
                
                //Obtains the typed dependency
                TreebankLanguagePack tlp = new PennTreebankLanguagePack();
                GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
                GrammaticalStructure gs = gsf.newGrammaticalStructure(sentenceTree);
                List<TypedDependency> tdl = gs.typedDependenciesCCprocessed();
                Iterator<TypedDependency> itdl = tdl.iterator();
                while(itdl.hasNext()) {
                    //TODO: Obtain here the DPRL
                    //String dep = itdl.next().reln().getShortName();
                    itdl.next();
                }
                                
                /*for (List<HasWord> sentence : sentences) {
                    System.out.println("SENTENCE: "+sentence);
                    ArrayList<TaggedWord> tSentence = tagger.tagSentence(sentence);
                    System.out.println(Sentence.listToString(tSentence, false));
                }*/
                
                //Extract the features of the Sentence, for each SI (k)
                for(int k = 0; k < sentenceSpatialIndicators.size(); k++){    
                    
                    ///////////////////////
                    // SI Classification //
                    ///////////////////////
                    
                    //f2: The SI Features vector
                    Map<String, Object> f2 = new HashMap<String, Object>();
                    //SI features
                    f2.put("SPATIAL_INDICATOR", sentenceSpatialIndicators.get(k));
                    f2.put("SPATIAL_INDICATOR_LEMMA", stemmer.stem(sentenceSpatialIndicators.get(k)));
                    f2.put("SPATIAL_INDICATOR_POS", tSentence.get(sentenceSpatialIndicatorsIndex.get(k)).tag());
                    for(int idx = sentenceSpatialIndicatorsIndex.get(k)-1; idx >=0; idx--){
                        //HEAD1 features, searches to the left part
                        if(listHeadWordsPOS.contains(tSentence.get(idx).tag())){
                            f2.put("HEAD1", tSentence.get(idx).word());
                            f2.put("HEAD1_POS", tSentence.get(idx).tag());                                                        
                            f2.put("HEAD1_LEMMA", stemmer.stem(tSentence.get(idx).word()));
                            break;
                        }
                    }
                    for(int idx = sentenceSpatialIndicatorsIndex.get(k)+sentenceSpatialIndicatorsWordCount.get(k); idx < tSentence.size(); idx++){
                        //HEAD2 features, searches to the right part
                        if(listHeadWordsPOS.contains(tSentence.get(idx).tag())){
                            f2.put("HEAD2", tSentence.get(idx).word());
                            f2.put("HEAD2_POS", tSentence.get(idx).tag());                            
                            f2.put("HEAD2_LEMMA", stemmer.stem(tSentence.get(idx).word()));
                            break;
                        }
                    }
                    
                    //Show the positive vector features
                    if(TRACE) System.out.println("VECTOR(+) = " + f2);
                    //Learn this instance
                    classifier.learn(f2, "SI");
                    
                    ///////////////////////////////////////////
                    // Trajector and Landmark Classification //
                    ///////////////////////////////////////////
                                      
                    for(int w=0;w<tSentence.size();w++) {
                        //f1 : Features of a word w - f1(w)
                        Map<String, Object> f1 = new HashMap<String, Object>();
                        //SI features
                        f1.put("WORD_FORM", tSentence.get(w).word());
                        f1.put("WORD_POS", tSentence.get(w).tag());
                        //TODO: Dependency with the head on syntactic tree
                     
                        //f1.put("WORD_DPRL", );
                        
                        //System.out.println(tdl);
                        
                        //f3 : Relation features between w and SI (k)
                        //TODO: PATH
                        //TODO: Binary linear position of w respect SI (k)
                        //TODO: Distance??
                    }
                }
                
                //Get the negative examples   
                //Checks if sentence contains one of the possible SI
                //that is not evaluated on the positive examples
                for(int k = 0; k < possibleSpatialIndicators.size(); k++){
                    for(int idx = 0; idx < tSentence.size() - possibleSpatialIndicators.get(k).size(); idx++){
                        if(!sentenceSpatialIndicatorsIndex.contains(idx)){
                            boolean match = true;
                            for(int iter = 0; iter < possibleSpatialIndicators.get(k).size(); iter++){
                                //If is not a possible SI, does not match
                                if(!tSentence.get(idx+iter).word().equals(possibleSpatialIndicators.get(k).get(iter))){
                                    match = false;
                                }
                            }
                            //SI has been found, is considered as a negative example
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
                                //Show the negative vector features
                                if(TRACE) System.out.println("VECTOR(-) = " + featureVector);
                                //Learn this instance
                                classifier.learn(featureVector, "NSI");                              
                            }
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

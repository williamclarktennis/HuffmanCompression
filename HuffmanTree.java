
import java.util.PriorityQueue;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * Solution to HW 7
 * 
 * Authors- Axel Fries and William Clark
 * 
 * Time Spent- 10 hours
 */
public class HuffmanTree{
    private HuffmanNode root;

    private static final int CHAR_MAX = 256; //1 more than the max ascii element

     /** Constructs a Huffman coding tree using the given array of frequencies, 
     * where count[i] is the number of occurrences of the character with the 
     * ASCII value i.
     * 
     * @param count - integer array with number of occurences of characters as the elements 
     * and ASCII value for the index
     */
    public HuffmanTree(int[] count){
        
        PriorityQueue<HuffmanNode> forest = new PriorityQueue<HuffmanNode>();

        for (int i = 0; i < CHAR_MAX; i++){
            if (count[i] > 0){
                forest.add(new HuffmanNode(count[i], i));
            }
        }
        forest.add(new HuffmanNode(1, CHAR_MAX));

        while (forest.size() > 1){

            HuffmanNode temp1 = forest.poll();
            HuffmanNode temp2 = forest.poll();
            int sum = temp1.freq + temp2.freq;

            HuffmanNode sumNode = new HuffmanNode(sum, -1);

            sumNode.left = temp1;
            sumNode.right = temp2;

            root = sumNode;

            forest.add(sumNode);
        } 
    }
    
    /**
     * HuffmanNode with attributes neccessary for Huffman encoding and decoding.
     */
    private class HuffmanNode implements Comparable<HuffmanNode> {
       
        private HuffmanNode right;
        private HuffmanNode left;
        
        private int ascii;
        private int freq; 

        /**
         * Constructor of a HuffmanNode
         * @param frequency - integer for the frequency of an ASCII value
         * @param ascii -integer for ASCII value
         */
        public HuffmanNode(int frequency, int ascii){
            right = null;
            left = null;
            //parent = null;

            this.ascii = ascii;
            this.freq = frequency;  
        }

        /**
         * Constructor used for decoding. 
         * @param ascii - integer for the ASCII value
         */
        public HuffmanNode(int ascii){
            right = null;
            left = null;

            this.ascii = ascii;
        }
        /**
         * Outputs the ascii code and the frequency of a this HuffmanNode
         */
        public String toString() {
            return "[" + freq + ", " + ascii + "]";
		}

       /**
        * Orders frequencies from low to high
        */
        public int compareTo(HuffmanNode o){
            return this.freq - o.freq;
        }

    }

    /**
     * Writes Huffman encoding to a given output PrintStream
     * @param output - the PrintStream to output the Huffman encoding to
     */
    public void write(PrintStream output){
        String bit = "";
        write(root, output, bit);
    }

    /**
     * Helper method for write.
     * @param currentRoot - HuffmanNode has acts as the current root of the HuffmanTree subtree
     * @param output - PrintStream to output to
     * @param bit - left edges are 0 and right edges are 1 for the binary search tree
     */
    private void write(HuffmanNode currentRoot, PrintStream output, String bit){
        if (currentRoot == null){
            return;
        }
        if (currentRoot.ascii > 0){
            output.println(currentRoot.ascii);
            output.println(bit);
        }
        write(currentRoot.left, output, bit+ "0");
        write(currentRoot.right, output, bit + "1");
    }

    /**
     * Constructs a Huffman tree from a file that contains the description 
     * of a tree stored in the format specified in the hw7.pdf Implementation Details section. 
     * You may assume that the supplied input stream is valid and already open.
     * 
     * @param input - Scanner that reads bits from an input file
     */
    public HuffmanTree(Scanner input){

        root = new HuffmanNode(-1);
        HuffmanNode currentRoot = root;  

        while (input.hasNextLine()){

            int n = Integer.parseInt(input.nextLine()); // reads the character 
            String code = input.nextLine(); // reads the corresponding Huffman code


            for (int i= 0; i < code.length(); i++){
                
                if (code.charAt(i) == '0'){
                    if (currentRoot.left != null){
                        currentRoot = currentRoot.left;
                    } else {
                        HuffmanNode left = new HuffmanNode(-1);
                        currentRoot.left = left;
                        currentRoot = currentRoot.left;
                    }
                    
                } else {
                    if (currentRoot.right != null){
                        currentRoot = currentRoot.right;
                    }
                    else{
                        HuffmanNode right = new HuffmanNode(-1);
                        currentRoot.right = right;
                        currentRoot = currentRoot.right;
                    }
                }
            }
            //now at the leaf:
            currentRoot.ascii = n;
            currentRoot = root;
        }

    }

    /** Reads the individual bits from the input
     * stream and writes the corresponding characters to the supplied output stream. It stops
     * reading when a character with a value equal to the eof parameter is encountered.
     *
     * @param input - BitInputStream to be decoded
     * @param output - PrintStream to be output to 
     * @param eof - integer of the end of file marker
     */
    public void decode(BitInputStream input, PrintStream output, int eof){

        HuffmanNode currentRoot;
        currentRoot = root;

        while (true){
            
            int n = input.readBit();

            if (n == 0){
                currentRoot = currentRoot.left;
            } else {
                currentRoot = currentRoot.right;
            }

            if (currentRoot.left == null && currentRoot.right == null){
                
                if (currentRoot.ascii == eof){ 
                    break;
                }
                output.write(currentRoot.ascii);
                currentRoot = root;
            }      
        }
    }

    /**
	 * Prints elements level-by-level (very useful for debugging).
	 */
	private void printTree() {
		if(root != null) {
			printInOrder(root, 0);
		}
	}

	/**
	 * Prints a subtree in-order with indentation.
	 * 
	 * @param currentRoot The subtree being printed.
	 * @param indentLevel The level of indentation in which this subtree should be printed.
	 */
	private void printInOrder(HuffmanNode currentRoot, int indentLevel) {
		if(currentRoot == null) {
			return;
		}

		printInOrder(currentRoot.right, indentLevel + 1);
		for(int i = 0; i < indentLevel; i++) {
			System.out.print("   ");
		}
		System.out.println(currentRoot);
		printInOrder(currentRoot.left, indentLevel + 1);

    }
    
    public static void main(String[] args){
        int[] count = new int[256];
        count[103] = 1;
        count[111] = 1;
        count[112] = 1;
        count[101]=  1;
        count[104] = 1;
        count[32] = 2;
        count[114] = 3;
        count[115] = 3;
        
        HuffmanTree test = new HuffmanTree(count);

        test.printTree();

    }

}
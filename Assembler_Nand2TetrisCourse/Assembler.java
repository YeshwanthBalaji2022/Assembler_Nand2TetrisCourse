import java.io.*;
import java.util.*;

public class Assembler {
    private static HashMap<String,Integer> symboltable;

    static {
        symboltable = new HashMap<String, Integer>();
        symboltable.put("SP",0);
        symboltable.put("LCL",1);
        symboltable.put("ARG",2);
        symboltable.put("THIS",3);
        symboltable.put("THAT",4);
        symboltable.put("R0",0);
        symboltable.put("R1",1);
        symboltable.put("R2",2);
        symboltable.put("R3",3);
        symboltable.put("R4",4);
        symboltable.put("R5",5);
        symboltable.put("R6",6);
        symboltable.put("R7",7);
        symboltable.put("R8",8);
        symboltable.put("R9",9);
        symboltable.put("R10",10);
        symboltable.put("R11",11);
        symboltable.put("R12",12);
        symboltable.put("R13",13);
        symboltable.put("R14",14);
        symboltable.put("R15",15);
        symboltable.put("SCREEN",16384);
        symboltable.put("KBD",24576);
    }

    private static final Map<String, String> DEST_MAP = new HashMap<String, String>();
    private static final Map<String, String> COMP_MAP = new HashMap<String, String>();
    private static final Map<String, String> JUMP_MAP = new HashMap<String, String>();



    static {
        DEST_MAP.put("", "000");
        DEST_MAP.put("M", "001");
        DEST_MAP.put("D", "010");
        DEST_MAP.put("MD", "011");
        DEST_MAP.put("A", "100");
        DEST_MAP.put("AM", "101");
        DEST_MAP.put("AD", "110");
        DEST_MAP.put("AMD", "111");

        COMP_MAP.put("0", "0101010");
        COMP_MAP.put("1", "0111111");
        COMP_MAP.put("-1", "0111010");
        COMP_MAP.put("D", "0001100");
        COMP_MAP.put("A", "0110000");
        COMP_MAP.put("M","1110000");
        COMP_MAP.put("!D", "0001101");
        COMP_MAP.put("!A", "0110001");
        COMP_MAP.put("!M","1110001");
        COMP_MAP.put("-D", "0001111");
        COMP_MAP.put("-A", "0110011");
        COMP_MAP.put("D+1", "0011111");
        COMP_MAP.put("A+1", "0110111");
        COMP_MAP.put("M+1","1110111");
        COMP_MAP.put("D-1", "0001110");
        COMP_MAP.put("A-1", "0110010");
        COMP_MAP.put("M-1","1110010");
        COMP_MAP.put("D+A", "0000010");
        COMP_MAP.put("A+D", "0000010");
        COMP_MAP.put("D+M","1000010");
        COMP_MAP.put("M+D","1000010");
        COMP_MAP.put("D-A", "0010011");
        COMP_MAP.put("D-M","1010011");
        COMP_MAP.put("A-D", "0000111");
        COMP_MAP.put("M-D","1000111");
        COMP_MAP.put("D&A", "0000000");
        COMP_MAP.put("D&M","1000000");
        COMP_MAP.put("D|A", "0010101");
        COMP_MAP.put("D|M","1010101");

        JUMP_MAP.put("", "000");
        JUMP_MAP.put("JGT", "001");
        JUMP_MAP.put("JEQ", "010");
        JUMP_MAP.put("JGE", "011");
        JUMP_MAP.put("JLT", "100");
        JUMP_MAP.put("JNE", "101");
        JUMP_MAP.put("JLE", "110");
        JUMP_MAP.put("JMP", "111");
    }
    private static List<String> preprocess(String fileName) throws IOException {
        List<String> lines = new ArrayList<String>();
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.length() > 0 && !line.startsWith("//")) {
                if (line.contains("//")) {
                    line = line.substring(0, line.indexOf("//")).trim();
                }
                lines.add(line);
            }
        }
        reader.close();
        return lines;
    }


    private static List<String> binaryconversion(List<String> lines) {
        List<String> binaryLines = new ArrayList<String>();
        int address = 0;
        for (String line : lines) {

            if (line.startsWith("(") & line.endsWith(")")) {
                symboltable.put(line.substring(1, line.length() - 1), address);
            } else {
                address++;
            }

        }

        int variableAddress = 16;
        for (String line : lines) {
            if (line.startsWith("(")) {
                continue;
            }

            if (line.startsWith("@")) {
                String symbol = line.substring(1);
                if (symbol.matches("\\d+")) {
                    binaryLines.add(String.format("%16s", Integer.toBinaryString(Integer.parseInt(symbol))).replace(' ', '0'));
                }
                 else if (symboltable.containsKey(symbol)) {
                    binaryLines.add(String.format("%16s", Integer.toBinaryString(symboltable.get(symbol))).replace(' ', '0'));
                }

                 else {

                    symboltable.put(symbol, variableAddress++);
                    binaryLines.add(String.format("%16s", Integer.toBinaryString(symboltable.get(symbol))).replace(' ', '0'));
                }

            } else {
                String dest = "000";
                String comp ;
                String jump = "000";
                if (line.contains("=")) {
                    dest = DEST_MAP.get(line.split("=")[0]);
                    line = line.split("=")[1];
                }
                if (line.contains(";")) {
                    comp = COMP_MAP.get(line.split(";")[0]);
                    jump = JUMP_MAP.get(line.split(";")[1]);
                } else {
                    comp = COMP_MAP.get(line);
                }
                binaryLines.add("111" + comp + dest + jump);
            }

        }
        System.out.println(binaryLines);
//        System.out.println(symbolTable);
          return binaryLines;
    }

    private static void writeBinaryFile(List<String> binaryLines, String fileName) throws IOException {
        FileWriter writer = new FileWriter(fileName);
        for (String binaryLine : binaryLines) {
            writer.write(binaryLine + "\n");
        }
        writer.close();
    }

    public static void main(String[] args) throws IOException {
        List<String> asmlines = preprocess("Pong.asm");
        List<String> asmbinarylines = binaryconversion(asmlines);

        writeBinaryFile(asmbinarylines, "Pong.hack");
        }
    }

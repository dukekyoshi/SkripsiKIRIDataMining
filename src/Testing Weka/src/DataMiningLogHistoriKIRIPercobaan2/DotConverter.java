/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataMiningLogHistoriKIRIPercobaan2;

/**
 *
 * @author Jovan Gunawan
 */
public class DotConverter 
{
    // converter dot khusus untuk skripsi data mining log histori KIRI --> output berupa tree dalam string dari weka
    public static String convert(SDForConvertTree data, SDForExtractData extract, String miningAlgo, int deep, String nodeName)
    {
        String result = "";
        String [] temp;
        String nodeName1="", nodeName2="";
        //color 1  selalu 1.0 karena merah
        double color; 
        if(miningAlgo.equals("id3"))
        {
            boolean hasNext = true;
            int loop = 0;
            while(hasNext)
            {
                hasNext = false;
                
                temp = data.getData(0).split("  ");
                boolean iniName1 = false;

                System.out.println("Deep: " + deep + data.getData(0));
                temp = temp[deep].split(" ");
                
                color = 0.7 - deep * 0.02;
                if(color < 0.3)
                {
                    color = 0.3;
                }

                if(nodeName.equals(""))
                {
                    if(loop == 0)
                    {
                        nodeName1 = data.getDataNumber(temp[0]);
                    }
                    result += nodeName1 + " -> ";
                    iniName1 = true;
                }
                else
                {
                    result += nodeName + " -> ";
                }

                if(temp[2].charAt(temp[2].length()-1) == ':')
                {
                    // menghasilkan daun
                    // String temp3 = temp[2].substring(0, temp[2].length()-1);
                    nodeName2 = data.getDataNumber(temp[3]);
                    result += nodeName2 + " [label=\"" + temp[1] + " " + temp[2].substring(0, temp[2].length()-1) +  "\"]\n";
                    if(iniName1 && loop == 0)
                    {
                        result += nodeName1 + " [label=\"" + temp[0] + "\",shape=box,style=filled,color=\"1.0 " + color + " 1.0\"]\n";
                    }
                    result += nodeName2 + " [label=\"" + temp[3] + "\"]\n";
                    data.buangArrayPertama();
                }
                else
                {
                    // menghasilkan node
                    String [] temp2;
                    temp2 = data.getData(1).split("  ");
                    temp2 = temp2[deep+1].split(" ");
                    nodeName2 = data.getDataNumber(temp2[0]);
                    result += nodeName2 + " [label=\"" + temp[1] + " " + temp[2] +  "\",shape=box,style=filled,color=\"1.0 " + color + " 1.0\"]\n";
                    data.buangArrayPertama();
                    //result += DotConverter.convert(data, miningAlgo, deep+1, nodeName2);

                    if(iniName1 && loop == 0)
                    {
                        result += nodeName1 + " [label=\"" + temp[0] + "\",shape=box,style=filled,color=\"1.0 " + color + " 1.0\"]\n";
                    }
                    result += nodeName2 + " [label=\"" + temp2[0] + "\",shape=box,style=filled,color=\"1.0 " + color + " 1.0\"]\n";
                }
                if(data.hasNext())
                {
                    if(data.getData(0).split("  ").length-1 == deep)
                    {
                        hasNext = true;
                    }
                }
                loop++;
            }
        }
        else
        {
            for(int i = 0; i < 2; i++)
            {
                temp = data.getData(0).split("   ");
                boolean iniName1 = false;

                System.out.println("Deep: " + deep + data.getData(0));
                temp = temp[deep].split(" ");

                color = 0.7 - deep * 0.02;
                if(color < 0.3)
                {
                    color = 0.3;
                }

                if(nodeName.equals(""))
                {
                    if(i == 0)
                    {
                        nodeName1 = data.getDataNumber(temp[0]);
                    }
                    result += nodeName1 + " -> ";
                    iniName1 = true;
                }
                else
                {
                    result += nodeName + " -> ";
                }
                
                if(temp[2].charAt(temp[2].length()-1) == ':')
                {   
                    // masukin data bwt extract
                    System.out.println("Check " + temp[2].substring(0, temp[2].length()-1));
                    extract.setRules(temp[0], temp[1], Integer.parseInt(temp[2].substring(0, temp[2].length()-1)));
                    extract.setKelas(Integer.parseInt(temp[3]));
                    extract.extract();
                    
                    // menghasilkan daun
                    // String temp3 = temp[2].substring(0, temp[2].length()-1);
                    nodeName2 = data.getDataNumber(temp[3]);
                    result += nodeName2 + " [label=\"" + temp[1] + " " + temp[2].substring(0, temp[2].length()-1) +  "\"]\n";
                    if(iniName1 && i == 0)
                    {
                        result += nodeName1 + " [label=\"" + temp[0] + "\",shape=box,style=filled,color=\"1.0 " + color + " 1.0\"]\n";
                    }
                    result += nodeName2 + " [label=\"" + temp[3] + "\"]\n";
                    data.buangArrayPertama();
                }
                else
                {
                    // masukin data bwt extract
                    extract.setRules(temp[0], temp[1], Integer.parseInt(temp[2]));
                    // menghasilkan node
                    String [] temp2;
                    temp2 = data.getData(1).split("   ");
                    temp2 = temp2[deep+1].split(" ");
                    nodeName2 = data.getDataNumber(temp2[0]);
                    result += nodeName2 + " [label=\"" + temp[1] + " " + temp[2] +  "\",shape=box,style=filled,color=\"1.0 " + color + " 1.0\"]\n";
                    data.buangArrayPertama();
                    SDForExtractData newExtract = new SDForExtractData(extract);
                    result += DotConverter.convert(data, newExtract, miningAlgo, deep+1, nodeName2);

                    if(iniName1 && i == 0)
                    {
                        result += nodeName1 + " [label=\"" + temp[0] + "\",shape=box,style=filled,color=\"1.0 " + color + " 1.0\"]\n";
                    }
                    result += nodeName2 + " [label=\"" + temp2[0] + "\",shape=box,style=filled,color=\"1.0 " + color + " 1.0\"]\n";
                    extract.addStringResult(newExtract.getList());
                }
            }
        }
        return result;
    }
    
    public static void main(String [] args)
    {
//        String test = "|    |    a <= 2: 1";
//        String [] temp = test.split("    ");
//        for(int i = 0; i < temp.length; i++)
//        {
//            System.out.println(temp[i]);
//        }
//        
//        String test = "tanggal <= 2\n" +
//                      "|   jam <= 2\n" +
//                      "|   |   menit <= 40\n" +
//                      "|   |   |   tanggal <= 1\n" +
//                      "|   |   |   |   jam <= 0\n" +
//                      "|   |   |   |   |   menit <= 14: 2 (2.0)";
//        
//        SDForConvertTree data = new SDForConvertTree(test.split("\n"));
//        
//        System.out.println(DotConverter.convert(data, 0));
    }
}

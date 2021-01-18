import java.io.*;

public abstract class Document{
    private File doc;

    public Document(String name){
        try {
            doc = new File(name);
        }catch(Exception e){
            System.err.println("Document: " + e);
        }
    }

    public void readFile(){
        try {
            // Se crea un lector del archivo y un buffer
            // que contendrá el texto del archivo
            FileReader fr = new FileReader(doc);
            BufferedReader br = new BufferedReader(fr);

            // Se lee linea por linea el archivo
            String linea;
            linea = br.readLine();
            while(linea != null){
                System.out.println(linea);
                linea = br.readLine();
            }

            // Se cierra el lector del archivo
            fr.close();
        }catch(Exception e){
            System.err.println("Document: " + e);
        }
    }

    public void writeFile(String line){
        try {
            // Se crea un escritor para poder plazmarlo
            // en el archivo nuevo
            FileWriter fw = new FileWriter(doc,true);
            PrintWriter pw = new PrintWriter(fw);

            pw.println(line);

            // Se cierra el lector y el escritor del archivo
            fw.close();
        }catch(Exception e){
            System.err.println("Document: " + e);
        }
    }
}
package Lector_Escritor;
/*
* @author : Barajas Figueroa Jose de Jesus, @jebarfig21
* Colaboraciones de Manuel Monreal(@mmonral) y Ricardo Badillo
* Cada lector y escritor se ejecuta en un hilo.
* Clase que resuleve el problema de los escritores y lectores
*/
import java.util.concurrent.ThreadLocalRandom;

enum Rol {ESCRITOR, LECTOR};


public class Lector_Escritor implements Runnable {
    public static int DEFAULT_NUMERO_ESCRITORES = 5; //Numero se escritores
    public static int DEFAULT_NUMERO_LECTORES = 5;   //Numero de lectores
    public static int numeroEscritores;              //Numero de escritores
    public static int numeroLectores;                //Numero de lectores
    public static int totalPersonas;                //Numero de lectores
    public static Monitor mon = new Monitor();      //Monitor en comun
    private Rol rol;
    private boolean escritorAdentro;
    private boolean lectorAdentro;


    /*
    * @param escritor : Bolean, True si es escritor, False si es lector
    * Constructor que recive true o false dependeinedo de ser escritor o lector
    */
    public Lector_Escritor(Rol rol){
      this.rol = rol;
      this.escritorAdentro = false;
      this.lectorAdentro = false;
    }

    /*
    * @return True : Si el objeto es escritor
    * @return False : Si el objeto es lector
    */
    public boolean esEscritor(){
         return rol == Rol.ESCRITOR ;
    }

    /*
    *Metodo run
    */
    public void run() {
      while(true){                                                                //Ejecucion Infinita

        if(esEscritor()){                                                         //Si el currentThread es escritor
          escritorAdentro = mon.entraEscritor(Thread.currentThread().getName());  //Solo entra un Thread ESCRITOR a escribir
            try{
              Thread.sleep((int)(Math.random()*1000));
            }catch(InterruptedException exc){}

            if(escritorAdentro){
             mon.saleEscritor(Thread.currentThread().getName());
             escritorAdentro = false;
            try{
              Thread.sleep((int)(Math.random()*1000));
            }catch(InterruptedException exc){}
           }

        }

        if(!esEscritor()){
          try{
            Thread.sleep((int)(Math.random()*1000));
          }catch(InterruptedException exc){}                                    //Si es lector, equiv a if(esLector())
          lectorAdentro = mon.entraLector(Thread.currentThread().getName());    //Entran a leer
        try{
          Thread.sleep((int)(Math.random()*1000));
        }catch(InterruptedException exc){}
          if(lectorAdentro){
            mon.saleLector(Thread.currentThread().getName());                   //Sale de leer
            lectorAdentro=false;
          }
        }

      }
    }

    /*
    *Metodo main
    */
    public static void main(String[] args){
      numeroEscritores = 5;                                                     //El numero de procesos escritores
      numeroLectores = 5;                                                       //El número de procesos lectores
      totalPersonas = numeroEscritores+numeroLectores;                          //Total de procesos
      Thread[] hilos = new Thread[totalPersonas];                               //Arreglo que va a contener los Threads

      //For para crear ecritores
      for(int i = 0; i< numeroEscritores ;i++){                                 //Creamos y lanzamos los procesos escritores
        Runnable runnable = new Lector_Escritor(Rol.ESCRITOR);
        hilos[i] = new Thread(runnable);
        hilos[i].setName("Escritor "+ (i+1));
        hilos[i].start();
      }

      //For para crear lectores
      for(int i = numeroEscritores; i<totalPersonas ;i++){                      //Creamos y lanzamos los procesos escritores
        Runnable runnable = new Lector_Escritor(Rol.LECTOR);
        hilos[i] = new Thread(runnable);
        hilos[i].setName("Lector "+ (i-numeroEscritores+1) );
        hilos[i].start();
      }

      for(int i = 0; i< totalPersonas ;i++){                                    //Join para los hilos
        try{
          hilos[i].join();
        }catch(Exception ex){}
      }

    }

}

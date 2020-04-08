package Lector_Escritor;
/*
* @author : Barajas Figueroa Jose de Jesus
* Colaboraciones de Manuel Monreal(@mmonral) y Ricardo Badillo
* @version : 0.1
* Clase que funciona como Monitor de comunicación para la clase Lector_Escritor
*/
public class Monitor{
  private int personas;                               //Numero de procesos dentro
  private boolean escribiendo;                        //True si hay un proceso escribiendo

  /*
  *@construc : Sin parametros, ningun proceso(personas) adentro, nadie esta escribiendo
  */
  public Monitor(){
    personas=0;
    escribiendo= false;
  }

  /*
  *Este metodo solo lo llaman los Threads que rol == Rol.ESCRITOR, un proceso entra a escribir
  *@param escritor : El nombre del proceso que entra
  */
  public synchronized boolean entraEscritor(String escritor){
    if (personas == 0 & !escribiendo){                                     //Si no hay personas
      personas++;                                                          //Aumenta en uno a las personas - el mismo
      escribiendo = true;                                                  //Se pone a escrbiir
      System.out.println("Entro"+ escritor +", personas: "+personas);
      System.out.println("El "+ escritor +" esta Escribiendo....");
      return true;                                                         //Si entró
    }

    else{                                                                  //Si no entra
      try{
        wait();                                                            //El prcoceso se va a dorimr
      }catch(Exception exc){}
    }
    return false;                                                          //No entró
  }


  /*
  *Este metodo solo lo llaman los Threads que rol == Rol.LECTOR, un proceso entra a leer
  *@param lector : El nombre del proceso que entra
  */
  public synchronized boolean entraLector(String lector){
    if (!escribiendo){                                                  //Si NO hay un escritor adentro
        personas++;                                                     //Entra a leer
        System.out.println("Entro "+lector+" , personas: " +personas);
        return true;
    }
    else{                                                              //Si hay algien escribiendo se va a dormir
        try{
          wait();
        }catch(Exception exc){}
    }
    return false;

  }

  /*
  *Este metodo solo lo llaman los Threads que rol == Rol.LECTOR
  *@param lector : El nombre del proceso que va a salir de leer
  */
  public synchronized void saleLector(String lector){
    if(personas>0){
        personas--;                                                             //Sale proceso
        System.out.println("Salio "+lector+ " ,personas: "+personas);
    }
    if(personas==0){                                                            //Cuando todos os procesos han salido
      notifyAll();                                                              //Todos los procesos se despiertan
    }
    try{
      wait();                                                                   //El proceso que sale duerme
    }catch(Exception exc){}

  }

  /*
  *Este metodo solo lo llaman los Threads que rol == Rol.ESCRITOR
  *@param escritor : El nombre del proceso que va a salir de leer
  */
  public synchronized void saleEscritor(String escritor){
      personas--;                                                               //Sale un proceso
      escribiendo = false;                                                      //Nadie escribe
      System.out.println("Salio el "+ escritor +", personas: "+ personas);
      notifyAll();                                                              //Todos los procesos se despiertan
      try{
        wait();                                                                 //El proceso que sale duerme
      }catch(Exception exc){}
  }
}

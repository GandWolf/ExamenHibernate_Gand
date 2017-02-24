import BD.Coches;
import BD.Propietarios;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Gand on 24/02/17.
 */
public class ExamenHibernate_Gand {

    static Scanner teclado;

    public static void main(String[] args) {
        System.out.println("***EXAMEN HIBERNATE GANDARA***");
        menu(args[0], args[1]);
        System.out.println("***FINALIZANDO EXAMEN HIBERNATE GANDARA***");
    }

    private static void menu(String arg, String s) {
        teclado = new Scanner(System.in);

        System.out.println("SELECCIONA UNA OPCION: ");
        System.out.println("1.- Recupera un propietario cuyo DNI se pasa a través del teclado");
        System.out.println("2.- Consultar datos de Propietarios que empiecen por M");
        System.out.println("3.- Datos de los coches de un Propietario dado su nombre");
        System.out.println("4.- Consultar varias filas con Query y Parametros, introduciendo datos por argumentos");
        System.out.println("5.- Consultar con funciones de grupo (count, max, sum)");
        System.out.println("6.- Actualizar el precio de los coches de dos formas");
        System.out.println("7.- Borrar coche con precio más bajo de dos formas");
        System.out.println("8.- Insertar registro en tabla Coches comprobando el Propietario");
        System.out.println("0.- Salir");

        char e = teclado.nextLine().charAt(0);

        switch (e){
            case '0': return;
            case '1': ejer1(); menu(arg, s);
                break;
            case '2': ejer2(); menu(arg, s);
                break;
            case '3': ejer3(); menu(arg, s);
                break;
            case '4': ejer4(arg, s); menu(arg, s);
                break;
            case '5': ejer5(); menu(arg, s);
                break;
            case '6': ejer6(); menu(arg, s);
                break;
            case '7': ejer7(); menu(arg, s);
                break;
            case '8': ejer8(); menu(arg, s);
                break;
            default:
                System.out.println("Debe introducir un número del 1 al 8");
                menu(arg, s);
        }
    }//menu

    static void ejer1(){
        teclado = new Scanner(System.in);
        Session session = HibernateUtil.getSessionFactory().openSession();

        System.out.println("Introduce el DNI del Propietario: ");
        String dni = teclado.nextLine();

        Propietarios propietario = session.get(Propietarios.class, dni);

        if (propietario == null)
            System.out.println("El DNI introducido no corresponde a ningún propietario");
        else
            System.out.println("DNI: " + propietario.getDni() + ", NOMBRE: " + propietario.getNombre() +
                ", EDAD: " + propietario.getEdad());

        session.close();
    }//1

    static void ejer2(){
        Session session = HibernateUtil.getSessionFactory().openSession();

        String consulta = "FROM Propietarios WHERE nombre like 'M%'";
        Query q = session.createQuery(consulta);
        List list = q.getResultList();

        for (Propietarios p : (List<Propietarios>)list){
            System.out.println("DNI: " + p.getDni() + ", NOMBRE: " + p.getNombre() + ", EDAD: " + p.getEdad());
        }
    }//2

    static void ejer3(){
        teclado = new Scanner(System.in);
        Session session = HibernateUtil.getSessionFactory().openSession();

        System.out.println("Introduce el NOMBRE del Porpietario: ");
        String nombre = teclado.nextLine();

        String consulta = "FROM Coches WHERE Coches.dni IN (SELECT dni FROM Propietarios WHERE nombre = " + nombre + ")";
        Query q = session.createQuery(consulta);

        System.out.println("Coches del Propietario: " + nombre);
        //Método list
        List<Coches> list = q.getResultList();
        for (Coches c : list){
            System.out.println("MATRICULA: " + c.getMatricula() + ", MARCA: " + c.getMarca()
                    + ", PRECIO: " + c.getPrecio());
        }
        //Método iterator
        Iterator iterator = q.iterate();
        while (iterator.hasNext()){
            Coches c = (Coches) iterator.next();
            System.out.println("MATRICULA: " + c.getMatricula() + ", MARCA: " + c.getMarca()
                    + ", PRECIO: " + c.getPrecio());
        }

        session.close();
    }//3

    static void ejer4(String nombreP, String matricula){
        Session session = HibernateUtil.getSessionFactory().openSession();

        String consultaP = "FROM Propietarios WHERE nombre = :nombre";
        Query q1 = session.createQuery(consultaP).setString("nombre", nombreP);
        List list = q1.getResultList();
        for (Propietarios p : (List<Propietarios>)list){
            System.out.println("DNI: " + p.getDni() + ", NOMBRE: " + p.getNombre() + ", EDAD: " + p.getEdad());
        }

        String consultaC = "FROM Coches WHERE matricula = ?";
        Query q2 = session.createQuery(consultaC).setString(0, matricula);
        List<Coches> list2 = q2.getResultList();
        for (Coches c : list2){
            System.out.println("MATRICULA: " + c.getMatricula() + ", MARCA: " + c.getMarca()
                    + ", PRECIO: " + c.getPrecio());
        }

        session.close();
    }//4

    static void ejer5(){
        teclado = new Scanner(System.in);
        Session session = HibernateUtil.getSessionFactory().openSession();

        //Numero de coches por DNI
        System.out.println("Introduce el DNI: ");
        String dni = teclado.nextLine();
        Propietarios propietario = session.get(Propietarios.class, dni);

        if (propietario == null)
            System.out.println("El DNI introducido no corresponde a ningún propietario");
        else
            System.out.println("DNI: " + propietario.getDni() + ", NOMBRE: " + propietario.getNombre() +
                    ", EDAD: " + propietario.getEdad());
        String consultaN = "SELECT count(Coches.matricula) FROM Coches WHERE Coches.DNI = '" + dni + "'";
        Query qN = session.createQuery(consultaN);
        int numeroCohces = qN.getFirstResult();
        System.out.println("Número de coches: " + numeroCohces);

        //Matricula más alta
        String consultaM = "FROM Coches WHERE matricula IN (SELECT max(matricula) FROM Coches )";
        Query qM = session.createQuery(consultaM);
        List<Coches> list = qM.getResultList();
        for (Coches c : list){
            System.out.println("MATRICULA: " + c.getMatricula() + ", MARCA: " + c.getMarca()
                    + ", PRECIO: " + c.getPrecio());
        }

        //Total gastado por cada propietario
        String consultaP = "FROM Propietarios";
        Query qP = session.createQuery(consultaP);
        List list2 = qP.getResultList();
        for (Propietarios p : (List<Propietarios>)list2){
            System.out.println("NOMBRE: " + p.getNombre() + ", TOTAL GASTADO: ");
            Query qT = session.createQuery("SELECT sum(precio) FROM Coches WHERE Coches.DNI = " + p.getDni());
            Integer precioTotal = qT.getFirstResult();
            System.out.print("" + precioTotal);
        }

        session.close();
    }//5

    static void ejer6(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        //Metodo 1 con HQL
        String update = "UPDATE Coches SET precio = precio + 1.01 WHERE precio < 2000";
        int filas = session.createQuery(update).executeUpdate();

        if (filas != 0)
            System.out.println("Precios actualizados");
        else
            System.out.println("No se ha modificado nada");

        //Metodo 2 con update
        String consulta = "FROM Coches WHERE precio < 2000";
        Query q = session.createQuery(consulta);
        List<Coches> list = q.getResultList();
        for (Coches c : list){
            c.setPrecio((int) (c.getPrecio()*1.01));
            session.update(c);
            tx.commit();
        }

        session.close();
    }//6

    static void ejer7(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        //Metodo 1 con HQL
        String delete = "DELETE FROM Coches WHERE precio IN (SELECT min(precio) FROM Coches)";
        int filas = session.createQuery(delete).executeUpdate();
        if (filas != 0)
            System.out.println("Coche borrado");
        else
            System.out.println("No se ha borrado nada");

        //Metodo 2 con delete
        String consulta = "FROM Coches WHERE precio IN (SELECT min(precio) FROM Coches)";
        Query q = session.createQuery(consulta);
        List<Coches> list = q.getResultList();
        for (Coches c : list){
            session.delete(c);
            tx.commit();
        }
        session.close();
    }//7

    static void ejer8(){
        teclado = new Scanner(System.in);
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        System.out.println("Introduce los datos del nuevo Coche: Matricula:");
        String matricula = teclado.nextLine();
        System.out.println("Introduce la marca del coche: ");
        String marca = teclado.nextLine();
        System.out.println("Introduce el precio del coche:");
        String precio = teclado.nextLine();
        System.out.println("Introduce DNI del propietario:");
        String DNI = teclado.nextLine();

        Propietarios p = session.get(Propietarios.class, DNI);
        if (p == null) {
            System.out.println("El propietario introducido NO existe");
            return;
        }

        //Método 1 con HQL
        String insert = "INSERT INTO Coches (" + matricula + ", " + marca + ", " + precio + ", " + DNI + ") " +
                "SELECT matricula, marca, precio, DNI FROM Coches ";
        int filas = session.createQuery(insert).executeUpdate();

        //Método 2 con save
        Coches c = new Coches(matricula, marca, precio, DNI);
        session.save(c);
        tx.commit();

        session.close();

    }//8
}
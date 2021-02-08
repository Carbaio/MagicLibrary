/**
 * Lo que he hecho hasta aqu� me ha servido para:
 * configurar el proyecto
 * Entender c�mo funciona, de forma b�sica jpa
 * 
 * Deuda t�cnica
 * No est� bien organizado, hay que pensar qu� managers vamos a tener y cu�l ser� el singleton, no tiene sentido que sean todos hay cosas repetidas, no hay arquitectura
 * tambi�n hay que organizar bien en paquetes DAO y CONTROL, hay que mirar alguna referencia para verlo bien
 */
package gitt.is.magiclibrary.model;

import java.util.List;
import java.util.logging.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import java.sql.Date;
import javax.persistence.PersistenceContext;
/**
 * @author Isabel Rom�n
 *
 */
@PersistenceContext(unitName = "h2-eclipselink")
public class TitleManager {
	private static final Logger log = Logger.getLogger(TitleManager.class.toString());
	private static EntityManager myEntityManager;
	private static EntityManager getEntityManager() {
		if (myEntityManager==null){
			try {
			log.info("Comienzo creando la factor�a de EntityManager\n");
			EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("h2-eclipselink");
			log.info("A continuaci�n creo un entityManager, usando la factor�a\n");
			myEntityManager = emf.createEntityManager();
			}
			catch(Throwable ex) {
				log.severe("Ha fallado la creaci�n de SessionFactory "+ex+"\n");
				throw new ExceptionInInitializerError(ex);
			}
		}
        log.info("Devuelvo el entityManager creado\n");
        return myEntityManager;
     }
	
	 public static void main(String[] args) {
		log.info("Comienzo una transacci�n con la BBDD\n");
        getEntityManager().getTransaction().begin();

        Title myBook = new Title("Ingenier�a del Software","Ian Sommerville", new Date(111,0,1));
        log.info("He creado el Title "+myBook+" y procedo a persistirlo");
     
        myEntityManager.persist(myBook);

        myBook = new Title("Ingenier�a del Software: un enfoque pr�ctico","Ian Roger S. Pressman", new Date(110,0,1));
        log.info("He creado el Title "+myBook+" y procedo a persistirlo");
        
      
        myEntityManager.persist(myBook);

        log.info("Finalizo la transacci�n con la base de datos\n");
        myEntityManager.getTransaction().commit();

      //  log.info("Invoco findById con primaryKey = '2L'");
       // Title found= findById(2L);
       // log.info("Recupero "+found);
        log.info("Invoco findAll");
        List<Title> encontrados=findAll();
        log.info("Recupero "+encontrados);
            
     //   log.info("Invoco findTitleByAuthor\n");
      //  Title found=findTitleByAuthor("Ian Roger S. Pressman");
       // log.info("Obtengo "+found);
     
	}
	
	 public static Title findById(java.lang.Object primaryKey) {
	        log.info("----\nBusco un Title usando el id "+primaryKey);
	        Title o = getEntityManager().find(Title.class, primaryKey);
	        log.info(o.toString());
	        return o;
	    }
	
	 public static Title findTitleByAuthor(String author) {
		   log.info("Consulto Title con author= "+author);
		   Query query = getEntityManager()
		         .createQuery("Select a from Title a where a.author = :author");
		   query.setParameter("author", author);
		   Title title = (Title) query.getSingleResult();
		   log.info("Resultado : " + title.toString());
		   return title;
		}

	  public static List<Title> findAll(){
	        log.info("----\nConsulta tipada usando JPQL");
	        TypedQuery<Title> q = getEntityManager().createQuery("select t from Title t", Title.class);
	        List<Title> resultList =q.getResultList();
	        log.info(q.getResultList().toString());
	        return resultList;
	    }
	  	
}

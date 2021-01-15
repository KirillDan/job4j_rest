package ru.job4j.react.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import ru.job4j.react.Observe;
import ru.job4j.react.hibernate.entity.Item;

import java.util.List;

public class HbmTracker implements Store, AutoCloseable {
    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    public Item add(Item item) {
    	Session session = this.sf.openSession();
    	session.beginTransaction();
    	session.save(item);
    	session.getTransaction().commit();
    	session.close();
        return item;
    }

    public boolean replace(String id, Item item) {
    	Session session = this.sf.openSession();
    	session.beginTransaction();
    	Item res = session.get(Item.class, Integer.valueOf(id));
    	res.setName(item.getName());
    	res.setDescription(item.getDescription());
    	res.setCreated(item.getCreated());
    	session.update(res);
    	session.getTransaction().commit();
    	session.close();
        return false;
    }

    public boolean delete(String id) {
    	Session session = this.sf.openSession();
    	session.beginTransaction();
    	Item item = new Item(null, null, null);
        item.setId(Integer.valueOf(id));
    	session.delete(item);
    	session.getTransaction().commit();
    	session.close();
        return false;
    }

    public void findAll(Observe<Item> observe) throws InterruptedException {
    	Session session = this.sf.openSession();
    	session.beginTransaction();
    	List<Integer> resultId = session.createQuery("SELECT i.id FROM Item i", Integer.class).getResultList();
    	for (Integer id : resultId) {
    		Item result = session.get(Item.class, id);
    		observe.receive(result);
		}
    	session.getTransaction().commit();
    	session.close();
    }

    public List<Item> findByName(String key) {
    	Session session = this.sf.openSession();
    	session.beginTransaction();
    	List<Item> result = session
    			.createQuery("SELECT i FROM Item i WHERE i.name = :name")
    			.setParameter("name", key).getResultList();
    	session.getTransaction().commit();
    	session.close();
        return result;
    }

    public Item findById(String id) {
    	Session session = this.sf.openSession();
    	session.beginTransaction();
    	Item result = session.get(Item.class, Integer.valueOf(id));
    	session.getTransaction().commit();
    	session.close();
        return result;
    }

    public void close() throws Exception {
        StandardServiceRegistryBuilder.destroy(registry);
    }
}

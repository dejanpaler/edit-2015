package com.ev3.item;

import java.time.Instant;
import java.util.Collection;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class Items {

    @PersistenceContext
    private EntityManager em;

    public Item createItem(String title, int coorX, int coorY, direction d) {
        // EntityManager em = emf.createEntityManager();
        Item newItem = new Item.Builder().id(ItemId.generate()).title(title)
                .created(Instant.now().toEpochMilli()).coorX(coorX).coorY(coorY).dir(d).build();
        em.persist(newItem);
        return newItem;
    }

    public Item findItem(String id) {
        // EntityManager em = emf.createEntityManager();
        return em.find(Item.class, id);
    }
    public Collection<Item>  findItemByTitle(String title){
        Query q = em.createQuery("SELECT i FROM Item i WHERE title='"+title+"'");
        return q.getResultList();
    }

    public Collection<Item> findAllItems() {
        // EntityManager em = emf.createEntityManager();
        Query q = em.createQuery("SELECT i FROM Item i");
        return q.getResultList();
    }
}
package com.ev3.item;

import java.time.Instant;
import java.util.Collection;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;

@Stateless
public class Items {

	@PersistenceContext(unitName = "Items")
	private EntityManager em;

	public Item createItem(String title) {
		//EntityManager em = emf.createEntityManager();
		Item newItem = new Item.Builder().id(ItemId.generate()).title(title).created(Instant.now().toEpochMilli()).build();
		em.persist(newItem);
		return newItem;
	}

	public Item findItem(String id) {
		//EntityManager em = emf.createEntityManager();
		return em.find(Item.class, id);
	}

	public Collection<Item> findAllItems() {
		//EntityManager em = emf.createEntityManager();
		Query q = em.createQuery("SELECT i FROM Item i");
		return (Collection<Item>) q.getResultList();
	}
}

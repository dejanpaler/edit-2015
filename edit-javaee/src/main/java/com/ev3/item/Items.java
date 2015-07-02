package com.ev3.item;

import java.time.Instant;
import java.util.Collection;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.spi.PersistenceProvider;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.ProviderUtil;

@Stateless
public class Items {
	
	@PersistenceContext(unitName="Items")
	EntityManager em;
	public Item createItem(String title) {
		Item newItem = new Item.Builder()
				.id(ItemId.generate())
				.title(title)
				.created(Instant.now().toEpochMilli())
				.build();
		
		em.getTransaction().begin();
		em.persist(newItem);
		em.close();
		return newItem;
	}
	
	public Item findItem(String id) {
		return em.find(Item.class, id);
	}
	
	public Collection<Item> findAllItems() {
		Query q = em.createQuery("SELECT * FROM items");
		return (Collection<Item>) q.getResultList();
	}
}

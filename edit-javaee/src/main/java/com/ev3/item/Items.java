package com.ev3.item;

import java.time.Instant;
import java.util.Collection;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class Items {
	
	public void findFirstEmptySpace()
	{
		Collection<Item> items = findAllItems();
	}
	
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

    public Collection<Item> findAllItems() {
        // EntityManager em = emf.createEntityManager();
        Query q = em.createQuery("SELECT i FROM Item i");
        return q.getResultList();
    }
    
    public boolean CheckFreeLocation(int row, int col, direction dir)
    {
    	String r = Integer.toString(row);
    	String c = Integer.toString(col);
    	String d = Integer.toString(dir.ordinal());
    	
    	Query q = em.createQuery("SELECT i FROM Item i WHERE coorX = '" + r + "' AND coorY = '" + c + "' AND dir = '" + d + "'");    	
    	
    	return q.getResultList().isEmpty();
    }
    
    public Location GetFreeLocation()
    {   	
    	// Hardcoded storage size.
    	int rows = 4;
    	int cols = 4;
    	
    	int r = (int)rows/2;
    	int c = (int)cols/2;
    	
    	for (int i = 1; i <= r; i++)
    	{
	    	for (int j = 1; j <= c; j++)
	    	{	    		
	    		if (CheckFreeLocation(i, -j, direction.down))
	    		{
	    			return new Location(i, -j, direction.down);
	    		}
	    		
	    		else if(CheckFreeLocation(i, j, direction.down))
	    		{
	    			return new Location(i, j, direction.down);
	    		}
	    		
	    		else if (CheckFreeLocation(i, -j, direction.up))
	    		{
	    			return new Location(i, -j, direction.up);
	    		}
	    		
	    		else if (CheckFreeLocation(i, j, direction.up))
	    		{
	    			return new Location(i, j, direction.up);
	    		}
	    	}
    	}
    	
    	return null;
    }
    
    public void AddItem(String title) throws Exception
    {    	
    	Location loc = GetFreeLocation();
    	
    	if (loc != null)
    	{    	
    		createItem(title, loc.getRow(), loc.getCol(), loc.getDirection());
    	}
    	else
    	{
    		throw new Exception("No free space in storage!");
    	}
    }
}

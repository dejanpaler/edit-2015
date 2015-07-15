package com.ev3.item;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class Item {
	
    @Id
    private String id;

    private String title;

    private long created;
    
    private int coorX;
    
    private int coorY;
    
    private int dir;
    
    public int getCoorX()
    {
    	return coorX;
    }
    
    public void setCoorX(int val)
    {
    	this.coorX = val;
    }
    
    public int getCoorY()
    {
    	return coorY;
    }
    
    public void setCoorY(int val)
    {
    	this.coorY = val;
    }
    
    public direction getDirection()
    {
    	return direction.values()[dir];
    }
    
    public void setDirection(direction d)
    {
    	this.dir = d.ordinal();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public boolean isValid() {
        return !id.isEmpty();
    }

    public static class Builder {
        private final Item item;

        public Builder() {
            this.item = new Item();
        }

        public Builder id(String id) {
            item.setId(id);
            return this;
        }

        public Builder title(String title) {
            item.setTitle(title);
            return this;
        }

        public Builder created(Long created) {
            item.setCreated(created);
            return this;
        }
        
        public Builder coorX(int coorx)
        {
        	item.setCoorX(coorx);
        	return this;
        }
        
        public Builder coorY(int coory)
        {
        	item.setCoorY(coory);
        	return this;
        }
        
        public Builder dir(direction d)
        {
        	item.setDirection(d);
        	return this;
        }

        public Item build() {
            return item;
        }
    }
}

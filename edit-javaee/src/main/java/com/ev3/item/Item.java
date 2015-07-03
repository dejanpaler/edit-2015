package com.ev3.item;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Item {

	@Id
	private String id;
	
	private String title;
	
	private long created;

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
		
		public Item build() {
			return item;
		}
	}
}

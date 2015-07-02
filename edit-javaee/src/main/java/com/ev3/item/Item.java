package com.ev3.item;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Item {

	@NotNull
	@Size(max=22, min=22)
	private String id;
	
	@NotNull
	@Size(max=100, min=3)
	private String title;
	
	@NotNull
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

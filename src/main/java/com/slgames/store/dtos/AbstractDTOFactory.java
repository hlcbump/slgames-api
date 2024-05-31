package com.slgames.store.dtos;

public interface AbstractDTOFactory <T>  {
	
	public abstract CreatedDTO fabricateCreated(T object);
	
	public abstract DefaultDTO fabricateDefault(T object);
}

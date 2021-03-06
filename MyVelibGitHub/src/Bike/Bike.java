package Bike;

import Card.Card;
import Time.Time;

/**
 * This class is providing the necessary function to be a bike, so it is defined as abstract and all the bike has been extended to this class.
 * @author Kevin HIVET 
 *
 */
public abstract class Bike implements BikeStrategyCardInterface {
	
	private int rentTime;
	private int ID;
	
	private static int uniqID = 0;
	
	public Bike() {
		super();
		this.rentTime = 0;
		uniqID++;
		this.ID = uniqID;
	}

	public Bike(int rentTime) {
		super();
		this.rentTime = rentTime;
		uniqID++;
		this.ID = uniqID;
	}
	
	public int getID() {
		return ID;
	}

	public int getRentTime() {
		return rentTime;
	}

	public void setRentTime(int rentTime) {
		this.rentTime = rentTime;
	}
	
	public abstract int getSpeed();

	public abstract int computeTotalPrice(Card card);
	
	@Override
	public int getDurationPrice() {
		int time1 = this.getRentTime();
		int time2 = Time.getTimeInMinuteSinceCreation();
		return Time.getDuration(time1,time2);
	}
	
}

package Test;

import static org.junit.Assert.*;

import org.junit.Test;

import Bike.ElectricalBike;
import Bike.MechanicalBike;
import Exception.NetworkFactoryException;
import Exception.UserFactoryException;
import GPSCoordinate.GPSCoordinate;
import Network.Network;
import Network.ParkingSlot;
import Network.PlusStation;
import Network.StandardStation;
import PlanningRide.FastestPath;
import PlanningRide.PreferPlusStationStrategy;
import Time.Time;
import User.User;
import User.UserFactory;

public class ScenarioTest {

	@Test
	public void destinationGetsFullScenario() throws UserFactoryException, NetworkFactoryException {
		//Building network
		Network paris = new Network("Paris", 30);
		
		PlusStation opera = new PlusStation(true, new GPSCoordinate(5,5), "Op�ra");
		for(int k = 0; k < 10; k++) {
			opera.addParkingSlot(new ParkingSlot(opera));
		}
		for(int k = 0; k < 4; k++) {
			opera.addBike(new MechanicalBike());
			opera.addBike(new ElectricalBike());

		}
		
		paris.addStation(opera);
		
		PlusStation chatelet = new PlusStation(true, new GPSCoordinate(11,17), "Ch�telet");
		for(int k = 0; k < 10; k++) {
			chatelet.addParkingSlot(new ParkingSlot(chatelet));
		}
		for(int k = 0; k < 4; k++) {
			chatelet.addBike(new MechanicalBike());
			chatelet.addBike(new ElectricalBike());
		}
		paris.addStation(chatelet);
		
		PlusStation luxembourg = new PlusStation(true, new GPSCoordinate(20,11), "Luxembourg");
		for(int k = 0; k < 10; k++) {
			luxembourg.addParkingSlot(new ParkingSlot(luxembourg));
		}
		for(int k = 0; k < 4; k++) {
			luxembourg.addBike(new MechanicalBike());
			luxembourg.addBike(new ElectricalBike());
		}
		paris.addStation(luxembourg);
		
		PlusStation odeon = new PlusStation(true, new GPSCoordinate(25,5), "Od�on");
		for(int k = 0; k < 10; k++) {
			odeon.addParkingSlot(new ParkingSlot(odeon));
		}
		for(int k = 0; k < 4; k++) {
			odeon.addBike(new MechanicalBike());
			odeon.addBike(new ElectricalBike());
		}
		paris.addStation(odeon);
		
		StandardStation montparnasse = new StandardStation(true, new GPSCoordinate(8,18), "Montparnasse");
		for(int k = 0; k < 10; k++) {
			montparnasse.addParkingSlot(new ParkingSlot(montparnasse));
		}
		for(int k = 0; k < 4; k++) {
			montparnasse.addBike(new MechanicalBike());
			montparnasse.addBike(new ElectricalBike());
		}
		paris.addStation(montparnasse);
		
		StandardStation invalides = new StandardStation(true, new GPSCoordinate(10,15), "Invalides");
		for(int k = 0; k < 10; k++) {
			invalides.addParkingSlot(new ParkingSlot(invalides));
		}
		for(int k = 0; k < 4; k++) {
			invalides.addBike(new MechanicalBike());
			invalides.addBike(new ElectricalBike());
		}
		paris.addStation(invalides);
		
		StandardStation defense = new StandardStation(true, new GPSCoordinate(24,4), "D�fense");
		for(int k = 0; k < 10; k++) {
			defense.addParkingSlot(new ParkingSlot(defense));
		}
		for(int k = 0; k < 4; k++) {
			defense.addBike(new MechanicalBike());
			defense.addBike(new ElectricalBike());
		}
		paris.addStation(defense);
		
		
		//Creating users

		User pierre = UserFactory.createUser("Pierre", "NoCard", "Paris");
		User paul = UserFactory.createUser("Paul", "Vlibre", "Paris");
		User jacques = UserFactory.createUser("Jacques", "Vmax", "Paris");
		
		
		//Doing stuff
		
		pierre.setLocation(4, 4);
		pierre.takeARide(pierre.getLocation(), new GPSCoordinate(21, 12), new FastestPath(), new MechanicalBike(), paris);
		assertEquals(pierre.getRide().getListStation().get(0), opera);
		assertEquals(pierre.getRide().getListStation().get(1), luxembourg);
		
		Time.addTime(2);
		
		opera.takeBike(new MechanicalBike(),  pierre);
		assertTrue(pierre.getRide().getRealBike() instanceof MechanicalBike);
		
		paul.setLocation(26,5);
		paul.takeARide(paul.getLocation(), new GPSCoordinate(21, 12), new PreferPlusStationStrategy(), new ElectricalBike(), paris);
		assertEquals(paul.getRide().getListStation().get(0), odeon);
		assertEquals(paul.getRide().getListStation().get(1), luxembourg);
		
		jacques.setLocation(24,4);
		jacques.takeARide(jacques.getLocation(), new GPSCoordinate(21, 12), new PreferPlusStationStrategy(), new ElectricalBike(), paris);
		assertEquals(jacques.getRide().getListStation().get(0), defense);
		assertEquals(jacques.getRide().getListStation().get(1), luxembourg);
		
		Time.addTime(2);
		
		odeon.takeBike(new ElectricalBike(),  paul);
		assertTrue(paul.getRide().getRealBike() instanceof ElectricalBike);
		
		defense.takeBike(new ElectricalBike(),  jacques);
		assertTrue(jacques.getRide().getRealBike() instanceof ElectricalBike);
		
		Time.addTime(5);
		
		assertTrue(luxembourg.isOnline());
		assertFalse(luxembourg.isFull());
		luxembourg.giveBackBike(paul);
		luxembourg.giveBackBike(jacques);
		
		assertTrue(luxembourg.isFull());
		
		Time.addTime(5);;
		
		odeon.giveBackBike(pierre);
		
		assertEquals(luxembourg.getUsers().size(), 0);
		assertEquals(pierre.getAllRide().get(pierre.getAllRide().size()-1).getPrice(), 1);
		
		User user1 = new User();
		User user2 = new User();
		
		chatelet.takeBike(new MechanicalBike(), user1);
		chatelet.takeBike(new MechanicalBike(), user2);
		
		pierre.takeARide(odeon.getCoordinates(), new GPSCoordinate(11,16), new PreferPlusStationStrategy(), new ElectricalBike(), paris);
		paul.takeARide(odeon.getCoordinates(), new GPSCoordinate(11,16), new PreferPlusStationStrategy(), new ElectricalBike(), paris);
		jacques.takeARide(odeon.getCoordinates(), new GPSCoordinate(11,16), new PreferPlusStationStrategy(), new ElectricalBike(), paris);
		assertEquals(pierre.getRide().getListStation().get(0), odeon);
		assertEquals(paul.getRide().getListStation().get(0), odeon);
		assertEquals(jacques.getRide().getListStation().get(0), odeon);
		assertEquals(pierre.getRide().getListStation().get(1), chatelet);
		assertEquals(paul.getRide().getListStation().get(1), chatelet);
		assertEquals(jacques.getRide().getListStation().get(1), chatelet);
		
		Time.addTime(2);
		
		odeon.takeBike(new ElectricalBike(), pierre);
		odeon.takeBike(new ElectricalBike(), paul);
		odeon.takeBike(new ElectricalBike(), jacques);
		
		Time.addTime(64);
		
		chatelet.giveBackBike(pierre);
		chatelet.giveBackBike(paul);
		chatelet.giveBackBike(jacques);
		
		assertEquals(pierre.getAllRide().get(pierre.getAllRide().size()-1).getPrice(), 4);
		assertEquals(paul.getAllRide().get(paul.getAllRide().size()-1).getPrice(), 1);	
		assertEquals(jacques.getAllRide().get(jacques.getAllRide().size()-1).getPrice(), 0);	

		
		
	}
	
	@Test
	public void destinationGoesOfflineScenario() throws UserFactoryException, NetworkFactoryException {
		//Building network
		Network paris = new Network("Paris", 30);
		
		PlusStation opera = new PlusStation(true, new GPSCoordinate(5,5), "Op�ra");
		for(int k = 0; k < 10; k++) {
			opera.addParkingSlot(new ParkingSlot(opera));
		}
		for(int k = 0; k < 4; k++) {
			opera.addBike(new MechanicalBike());
			opera.addBike(new ElectricalBike());

		}
		
		paris.addStation(opera);
		
		PlusStation chatelet = new PlusStation(true, new GPSCoordinate(11,17), "Ch�telet");
		for(int k = 0; k < 10; k++) {
			chatelet.addParkingSlot(new ParkingSlot(chatelet));
		}
		for(int k = 0; k < 4; k++) {
			chatelet.addBike(new MechanicalBike());
			chatelet.addBike(new ElectricalBike());
		}
		paris.addStation(chatelet);
		
		PlusStation luxembourg = new PlusStation(true, new GPSCoordinate(20,11), "Luxembourg");
		for(int k = 0; k < 10; k++) {
			luxembourg.addParkingSlot(new ParkingSlot(luxembourg));
		}
		for(int k = 0; k < 4; k++) {
			luxembourg.addBike(new MechanicalBike());
			luxembourg.addBike(new ElectricalBike());
		}
		paris.addStation(luxembourg);
		
		PlusStation odeon = new PlusStation(true, new GPSCoordinate(25,5), "Od�on");
		for(int k = 0; k < 10; k++) {
			odeon.addParkingSlot(new ParkingSlot(odeon));
		}
		for(int k = 0; k < 4; k++) {
			odeon.addBike(new MechanicalBike());
			odeon.addBike(new ElectricalBike());
		}
		paris.addStation(odeon);
		
		StandardStation montparnasse = new StandardStation(true, new GPSCoordinate(8,18), "Montparnasse");
		for(int k = 0; k < 10; k++) {
			montparnasse.addParkingSlot(new ParkingSlot(montparnasse));
		}
		for(int k = 0; k < 4; k++) {
			montparnasse.addBike(new MechanicalBike());
			montparnasse.addBike(new ElectricalBike());
		}
		paris.addStation(montparnasse);
		
		StandardStation invalides = new StandardStation(true, new GPSCoordinate(10,15), "Invalides");
		for(int k = 0; k < 10; k++) {
			invalides.addParkingSlot(new ParkingSlot(invalides));
		}
		for(int k = 0; k < 4; k++) {
			invalides.addBike(new MechanicalBike());
			invalides.addBike(new ElectricalBike());
		}
		paris.addStation(invalides);
		
		StandardStation defense = new StandardStation(true, new GPSCoordinate(24,4), "D�fense");
		for(int k = 0; k < 10; k++) {
			defense.addParkingSlot(new ParkingSlot(defense));
		}
		for(int k = 0; k < 4; k++) {
			defense.addBike(new MechanicalBike());
			defense.addBike(new ElectricalBike());
		}
		paris.addStation(defense);
		
		
		//Creating users

		User pierre = UserFactory.createUser("Pierre", "NoCard", "Paris");
			
		//Doing stuff
			
		pierre.setLocation(4, 4);
		pierre.takeARide(pierre.getLocation(), new GPSCoordinate(21, 12), new FastestPath(), new MechanicalBike(), paris);
		assertEquals(pierre.getRide().getListStation().get(0), opera);
		assertEquals(pierre.getRide().getListStation().get(1), luxembourg);
		
		Time.addTime(2);
		
		opera.takeBike(new MechanicalBike(), pierre);
		assertEquals(luxembourg.getUsers().size(), 1);
		
		Time.addTime(5);
		
		luxembourg.goOffline();
		
		Time.addTime(5);;
		
		odeon.giveBackBike(pierre);
		
		assertEquals(luxembourg.getUsers().size(), 0);
		assertEquals(pierre.getAllRide().get(pierre.getAllRide().size()-1).getPrice(), 1);
		
		pierre.takeARide(odeon.getCoordinates(), new GPSCoordinate(11,16), new PreferPlusStationStrategy(), new ElectricalBike(), paris);
		assertEquals(pierre.getRide().getListStation().get(0), odeon);
		assertEquals(pierre.getRide().getListStation().get(1), chatelet);
		
		Time.addTime(2);
		
		odeon.takeBike(new ElectricalBike(), pierre);
		
		Time.addTime(70);
		
		chatelet.giveBackBike(pierre);
		
		assertEquals(pierre.getAllRide().get(pierre.getAllRide().size()-1).getPrice(), 4);		
		
	}

}

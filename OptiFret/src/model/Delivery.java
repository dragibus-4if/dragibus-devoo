package model;

public class Delivery {

	private Long id;
	TimeSlot timeSlot;
	Client client;
	DeliveryPoint deliveryPoint;

	public Delivery() {
		// TODO - implement Delivery.Delivery
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param TimeSlot
	 */
	public void setTimeSlot(int TimeSlot) {
		// TODO - implement Delivery.setTimeSlot
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param DeliveryPoint
	 */
	public void setDeliveryPoint(int DeliveryPoint) {
		// TODO - implement Delivery.setDeliveryPoint
		throw new UnsupportedOperationException();
	}

	public TimeSlot getTimeSlot() {
		return this.timeSlot;
	}

	public DeliveryPoint getDeliveryPoint() {
		return this.deliveryPoint;
	}

}
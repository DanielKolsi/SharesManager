package com.sharesmanager.main;

import java.time.LocalDate;
import java.util.Comparator;

public class Entity implements Comparable<Entity> {
	
	private String id = null;
	private String instruction = null; // instruction to Buy (B) or Sell (S)
	private float agreedFx;
	private String currency; 
	private LocalDate instructionDate; // AFAIK, this information is not needed for the report
	private LocalDate settlementDate; // needs to be adjusted to the next working day if on "holiday"
	private int units;
	private float pricePerUnit;
	
	public Entity(String id) {
		this.id = id;		
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getInstruction() {
		return instruction;
	}
	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}
	public float getAgreedFx() {
		return agreedFx;
	}
	public void setAgreedFx(float agreedFx) {
		this.agreedFx = agreedFx;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public LocalDate getInstructionDate() {
		return instructionDate;
	}
	public void setInstructionDate(LocalDate instructionDate) {
		this.instructionDate = instructionDate;
	}
	public LocalDate getSettlementDate() {
		return settlementDate;
	}
	public void setSettlementDate(LocalDate settlementDate) {
		this.settlementDate = settlementDate;
	}
	public int getUnits() {
		return units;
	}
	public void setUnits(int units) {
		this.units = units;
	}
	public float getPricePerUnit() {
		return pricePerUnit;
	}
	public void setPricePerUnit(float pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}
	
	public double getUSDAmountOfATrade() {
		return getPricePerUnit() * getUnits() * getAgreedFx();
	}
	
	@Override
	public int compareTo(Entity compareEntity) {
		double compareAmount = compareEntity.getUSDAmountOfATrade();	
		return (int)(compareAmount - this.getUSDAmountOfATrade()); // descending order (highest amount first)
	}
	
	public static Comparator<Entity> EntitySettlementDateComparator = new Comparator<Entity>() {
		
		@Override
		public int compare(Entity entity1, Entity entity2) {
			
			LocalDate date1 = entity1.getSettlementDate();
			LocalDate date2 = entity2.getSettlementDate();		
			return date2.compareTo(date1); // newest settlement date first (on top of the report)
		 }
	};		
}
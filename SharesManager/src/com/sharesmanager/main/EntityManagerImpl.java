package com.sharesmanager.main;

import java.time.LocalDate; // Java8 feature
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EntityManagerImpl implements EntityManager  {
	
	private List<Entity> entities;
	
	private List<Entity> entitiesIncome = new ArrayList<>(); // sell entities
	private List<Entity> entitiesOutgoing = new ArrayList<>(); // buy entities
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM uuuu");
	

	public EntityManagerImpl(List<Entity> entities) {
		this.entities = entities;		
	}
	
	public List<Entity> getEntities() {
		return entities;
	}

	public List<Entity> getEntitiesIncome() {
		return entitiesIncome;
	}
	
	public List<Entity> getEntitiesOutgoing() {
		return entitiesOutgoing;
	}


	// sort entities between B & S and adjust their settlement date
	public void processEntities() {
		for (Entity e: this.entities) {
		    String id = e.getInstruction();
		    adjustSettlementDate(e);		    
			if (id == Properties.OUTGOING) {
				entitiesOutgoing.add(e);
			} else if (id == Properties.INCOME) {
				entitiesIncome.add(e);
			}
		}		
	}
		
	private int getWeekDayNumberForSettlement(Entity entity) {
		LocalDate date = entity.getSettlementDate();
		return date.getDayOfWeek().getValue(); // starting from 1 ( = Monday)
	}
	
	/**
	 * Ensure that the settlement will be always on a working day.
	 * @return Entity with adjusted date
	 */
	public Entity adjustSettlementDate(Entity entity) {
		String currency = entity.getCurrency();
		int weekDay = getWeekDayNumberForSettlement(entity);
				
		// work week from Sunday to Thursday 
		if (Properties.AED.equals(currency) || Properties.SAR.equals(currency)) {			
			if (weekDay == Properties.FRIDAY) { // Friday				
				LocalDate datePlusTwo = entity.getSettlementDate().plusDays(2); // advance two days
				entity.setSettlementDate(datePlusTwo); // advance settlement date to the next working day				
			} else if (weekDay == Properties.SATURDAY) { // Saturday
				LocalDate datePlusOne = entity.getSettlementDate().plusDays(1); // advance one day
				entity.setSettlementDate(datePlusOne); // advance settlement date to the next working day
			} 
		} else { // normal Monday-Friday work week
			if (weekDay == Properties.SATURDAY) { // Saturday
				LocalDate datePlusTwo = entity.getSettlementDate().plusDays(2); // advance two days
				entity.setSettlementDate(datePlusTwo); // advance settlement date to the next working day 
			} else if (weekDay == Properties.SUNDAY) { // Sunday
				LocalDate datePlusOne = entity.getSettlementDate().plusDays(1); // advance one day
				entity.setSettlementDate(datePlusOne); // advance settlement date to the next working day
			}
		}	
		return entity;
	}
	
	public void displayCombinedReport() {
		System.out.println("**** BEGINNING OF THE REPORT ****");
		createEntityIncomeReport();
		createEntityOutgoingReport();
		System.out.println("\n**** END OF THE REPORT ****\n\n\n");
	}
	
	/**
	 * @return consecutive sum of daily trade amount (settlement) from events each day (income or outgoing)
	 */
	public double processDailySumOfUSDAmountOfATrade(List<Entity> entities, String instruction) {
		
		Collections.sort(entities, Entity.EntitySettlementDateComparator);
		LocalDate previousEntityDate = null;
		double sum = 0;
				
		for (Entity e: entities) {
			 LocalDate settlementDate = e.getSettlementDate();
			 if (settlementDate.equals(previousEntityDate)) { // same date
				 sum+=e.getUSDAmountOfATrade(); // add the amount to the sum of the same date					 
			 } else {
				
				 if (sum > 0 && previousEntityDate != null) {
					 if (instruction == Properties.INCOME) { // income
						 System.out.println("Income settlement date: " + previousEntityDate.format(this.formatter) + " settled incoming (USD): " + sum + " (day total income) ");	 
					 } else if (instruction == Properties.OUTGOING) { // outgoing
						 System.out.println("Outgoing settlement date: " + previousEntityDate.format(this.formatter) + " settled incoming (USD): " + sum + " (day total Outgoing) "); 
					 }
					 	 
				 }
				 sum = e.getUSDAmountOfATrade(); // a new settlement date				 
			 }
			 previousEntityDate = settlementDate;						
		}
		
		if (previousEntityDate != null) { // ensure that the last settlement (or sum) will be outputted to the report!
			if (instruction == Properties.INCOME) { // income
				System.out.println("Income settlement date: " + previousEntityDate.format(this.formatter) + " settled incoming (USD): " + sum + " (day total income)");	
			} else if (instruction == Properties.OUTGOING) { // outgoing
				System.out.println("Outgoing settlement date: " + previousEntityDate.format(this.formatter) + " settled incoming (USD): " + sum + " (day total Outgoing)");
			}		
		}	
		return sum;
	}
	
	/**
	 * Create and output income report, where income entities are
	 * listed as highest income first.
	 * @return sorted list of income entities
	 */
	public List<Entity> createEntityIncomeReport() {
		System.out.println("\n$$$$$ ENTITY INCOME REPORT $$$$$ \n");
				
		if (this.entities != null && !this.entities.isEmpty() && this.entitiesIncome.isEmpty()) {			
			processEntities(); // ensure here that entities have been processed
		}
		
		processDailySumOfUSDAmountOfATrade(this.entitiesIncome, Properties.INCOME);
				
		Collections.sort(this.entitiesIncome);
		System.out.println("\n$$$$$ Income entities rates as highest settlement first $$$$$\n");
		
		int rank = 0; // entity rank
		for (Entity e: this.entitiesIncome) {			
			System.out.println("Rank:" + ++rank + " | Entity id: " + e.getId() + " | Amount of trade: " + e.getUSDAmountOfATrade() + " | Settlement date: " + e.getSettlementDate().format(this.formatter));			
		}
		
		return this.entitiesIncome; // sorted list of income entities 
	}
		
	/**
	 * Create and output outgoing report, where outgoing entities are
	 * listed as highest outgoing first.
	 * @return sorted list of outgoing entities
	 */
	public List<Entity> createEntityOutgoingReport() {
		
		System.out.println("\n\n$$$$$ ENTITY Outgoing REPORT $$$$$ \n");
		
		if (this.entities != null && !this.entities.isEmpty() && this.entitiesOutgoing.isEmpty()) {			
			processEntities(); // ensure here that entities have been processed
		}
		processDailySumOfUSDAmountOfATrade(this.entitiesOutgoing, Properties.OUTGOING);
		
		Collections.sort(this.entitiesOutgoing);
		System.out.println("\n$$$$$ Outgoing entities rates as highest settlement first $$$$$\n");
		int rank = 0; // entity rank
		
		for (Entity e: this.entitiesOutgoing) {
			System.out.println("Rank:" + ++rank + " | Entity id: " + e.getId() + " | Amount of trade: " + e.getUSDAmountOfATrade() + " | Settlement date: " + e.getSettlementDate().format(this.formatter));			
		}
		return this.entitiesOutgoing; // sorted list of outgoing entities 
	}	
}

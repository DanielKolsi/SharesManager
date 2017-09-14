package com.sharesmanager.main;

import java.util.List;

public interface EntityManager {
	
	/**
	 * Process all (B & S) entities, sort them between entitiesIncome 
	 * and entitiesoutgoing lists and adjust correct settlement date (working day)
	 * to each entity. Settlement date is adjusted (changed) if it happens to be a "holiday". 
	 */
	public void processEntities();

	
	public Entity adjustSettlementDate(Entity entity);
	
	/**
	 * Create a settlement amount report of income entities and output it.
	 * @return a sorted list of income entities, highest settlement first (Entity implements Comparable) 
	 */
	public List<Entity> createEntityIncomeReport();
	
	/**
	 * Create a settlement amount report of outgoing entities and output it.
	 * @return a sorted list of outgoing entities, highest settlement first (Entity implements Comparable) 
	 */
	public List<Entity> createEntityOutgoingReport();
	
	/**
	 * @param entities all Sell (S) or Buy (B) entities, based on transactionId
	 * @param transactionId "B" if Buy, "S" if Sell 
	 * @return daily sum of settlement amount from either S or B entities
	 */
	public double processDailySumOfUSDAmountOfATrade(List<Entity> entities, String transactionId);

}

package mutant.persistence;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;

import mutant.model.Stats;

public class MutantDynamoBDPersistence {
	
	  private DynamoDB dynamoDb;
	  private String DYNAMO_DB_TABLE_NAME = "MutantDNAAnalisys";
	  private Regions REGION = Regions.US_EAST_1;
	  public Table table;

	public void initDynamoDbClient() {
		  AmazonDynamoDBClient client = new AmazonDynamoDBClient();
		  client.setRegion(Region.getRegion(REGION));
		  this.dynamoDb = new DynamoDB(client);
		 }
	  
	  public boolean saveMutantAnalisys(String dna, boolean isMutant) {
		  table = dynamoDb.getTable(DYNAMO_DB_TABLE_NAME);
		  table.putItem(new PutItemSpec().withItem(
		    new Item().withString("dna", dna)
		               .withBoolean("isMutant", isMutant)));
		  return true;
		 }
	  
	  public void updateMutantAnalisysStats(boolean isMutant) {
		  table = dynamoDb.getTable(DYNAMO_DB_TABLE_NAME);
		  Stats actualstats = getStats();
		  Map<String, String> expressionAttributeNames = new HashMap<String, String>();
		  expressionAttributeNames.put("#NOA", "NumberofAnalisys");
		  expressionAttributeNames.put("#NOM", "NumberOfMutants");
		  Map<String, Object> expressionAttributeValues = new HashMap<String, Object>();
		  expressionAttributeValues.put(":valNOA", actualstats.getNumberofAnalisys()+1);  
		  if (isMutant) {
			  expressionAttributeValues.put(":valNOM", actualstats.getNumberOfMutants()+1); 
		  }else {
			  expressionAttributeValues.put(":valNOM", actualstats.getNumberOfMutants());  
		}
		  table.updateItem (new PrimaryKey("dna","count"),
				   "set #NOA = :valNOA,  #NOM = :valNOM",
				   expressionAttributeNames,
				   expressionAttributeValues);
		 }
	  
	  public Stats getStats() {
		  Stats stats = new Stats();
		  table = dynamoDb.getTable(DYNAMO_DB_TABLE_NAME);
		  QuerySpec spec = new QuerySpec()
				  .withKeyConditionExpression("dna = :v_dna")
				  .withValueMap(new ValueMap()
						  .withString(":v_dna", "count")
						  )
				  .withConsistentRead(true);		 
		  ItemCollection<QueryOutcome> items = table.query(spec);
		  for (Item item : items) {
			  stats.setNumberofAnalisys(item.getInt("NumberofAnalisys"));
		      stats.setNumberOfMutants(item.getInt("NumberOfMutants"));
		}
		  		  
		return stats;
		  
	  }

}

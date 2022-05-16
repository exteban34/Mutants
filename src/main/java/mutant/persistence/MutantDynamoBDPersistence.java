package mutant.persistence;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
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
	  
	  public void initDynamoDbClient() {
		  AmazonDynamoDBClient client = new AmazonDynamoDBClient();
		  client.setRegion(Region.getRegion(REGION));
		  this.dynamoDb = new DynamoDB(client);
		 }
	  
	  public boolean saveMutantAnalisys(String dna, boolean isMutant) {
		  Table table = dynamoDb.getTable(DYNAMO_DB_TABLE_NAME);
		  table.putItem(new PutItemSpec().withItem(
		    new Item().withString("dna", dna)
		               .withBoolean("isMutant", isMutant)));
		  return true;
		 }
	  
	  public Stats getStats() {
		  Stats stats = new Stats();
		  Table table = dynamoDb.getTable("ResultadosDivisiones");
		  QuerySpec spec = new QuerySpec()
				  .withKeyConditionExpression("resultado = :v_resultado")
				  .withFilterExpression("Denominador = :v_denominador")
				  .withValueMap(new ValueMap()
						  .withInt(":v_resultado", 20)
						  .withInt(":v_denominador", 1))
				  .withConsistentRead(true);		 
		  ItemCollection<QueryOutcome> items = table.query(spec);
//		  Iterator<Item> iterator = items.iterator();		  
//		  Item item = null;
		  for (Item item : items) {
			  stats.setNumberofAnalisys(item.getInt("Numerador"));
		      stats.setNumberOfMutants(item.getInt("resultado"));
		}
		
		return stats;
		  
	  }

}

package mutant.persistence;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;

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

}

package index;

import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;


import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;

public class QueryConstructor {

	
	public static Query construct(Int2IntOpenHashMap expressionInfo) {
		BooleanQuery.Builder bq = new BooleanQuery.Builder();
		
		
		BooleanQuery query = bq.build();
		
		return query;
	}
	
}

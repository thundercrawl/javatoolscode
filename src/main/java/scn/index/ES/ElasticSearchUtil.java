package scn.index.ES;

import java.io.Serializable;

import scn.index.status.CommonStatus;

public class ElasticSearchUtil implements Serializable{

	/*
	 * Hiding all setting for shard and replica
	 * shard and replica should distribute on the backend cluster by ES methodology
	 */
	public static CommonStatus CreateIndex(String idxName)
	{
		CommonStatus status = new CommonStatus();
		
		
		
		return status;
	}
}

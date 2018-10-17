package scn.index.domino.taskdef;

import lotus.domino.Document;

public interface DominoIndexer {

	public void insert(Object[] fields,Object values[]);
	public void update(Object[] fields, Object values[]);
}

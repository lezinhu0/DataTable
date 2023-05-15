package br.com.lsoft.modules;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DataTable implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private LinkedList<LinkedList<Object>> data;
	private LinkedList<Object> headers;
	
	{
		this.data = new LinkedList<LinkedList<Object>>();
		this.headers = new LinkedList<Object>();
	}

	public DataTable() {
	}

	public DataTable(LinkedList<LinkedList<Object>> data) {
		this.data = data;
	}

	public DataTable(LinkedList<LinkedList<Object>> data, LinkedList<Object> headers) {
		this.data = data;
		this.headers = headers;
	}

	public DataTable setData(LinkedList<LinkedList<Object>> data) {
		this.data = data;
		return this;
	}

	public LinkedList<LinkedList<Object>> getData() {
		return data;
	}

	public DataTable setHeaders(LinkedList<Object> headers) {
		this.headers = headers;
		return this;
	}

	public LinkedList<Object> getHeaders() {
		return headers;
	}

	public Object getField(int rowNumber, String columnName) {
		int index = this.headers.indexOf(columnName);
		if (index < 0) return null;
		
		return this.data.get(rowNumber).get(index);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getField(int rowNumber, String columnName, Class<T> type) {
		return (T) this.data.get(rowNumber).get(this.headers.indexOf(columnName));
	}

	public boolean isEmpty() {
		if (this.data.size() > 0) return false;
		
		return true;
	}

	public void append(DataTable table) {
		this.data.addAll(table.getData());
	}

	public LinkedHashMap<Object, LinkedHashMap<Object, Object>> toMap() {
		LinkedHashMap<Object, LinkedHashMap<Object, Object>> map = new LinkedHashMap<>();
		for (LinkedList<Object> linha : data) {
			LinkedHashMap<Object, Object> map2 = new LinkedHashMap<>();
			for (int i = 0; i < headers.size(); i++) {
				Object object = headers.get(i);
				map2.put(object, linha.get(i));
			}
			map.put(linha.get(0), map2);
		}
		return map;
	}

	public LinkedList<Object> getColumn(String header) {
		LinkedList<Object> columns = new LinkedList<>();
		int index = headers.indexOf(header);
		for (LinkedList<Object> list : data) {
			columns.add(list.get(index));
		}
		return columns;
	}

	public void moveRowToFinal(int rowNumber) {
		LinkedList<Object> row = data.get(0);
		data.remove(0);
		data.add(row);
	}

	@Override
	public String toString() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(this);
	}

	public void invertRowsAndColumns() {
		DataTable table = new DataTable();

		LinkedList<Object> tempHeaders = this.getColumn((String) this.headers.get(0));
		tempHeaders.add(0, this.headers.get(0));
		table.setHeaders(tempHeaders);
		
		for (int i = 1; i < headers.size(); i++) {
			String header = (String) headers.get(i);
			LinkedList<Object> column = this.getColumn(header);
			column.add(0, header);
			table.getData().add(column);
		}
		
		this.data = table.getData();
		this.headers = table.getHeaders();
	}

	public void removeColumn(int columnIndex) {
		headers.remove(columnIndex);
		for (int i = 0; i < data.size(); i++) {
			LinkedList<Object> linha = data.get(i);
			linha.remove(columnIndex);
		}
	}

	public void removeColumns(Integer start, Integer end) {
		for (int i = end; i >= start; i--) {
			this.headers.remove(i);
		}
		
		for (int i = 0; i < data.size(); i++) {
			LinkedList<Object> linha = data.get(i);
			for (int j = end; j >= start; j--) {
				linha.remove(j);
			}
		}
	}

	public BigDecimal getColumnSum(int index) {
		if (index >= headers.size()) {
			return BigDecimal.ZERO;
		}
		BigDecimal sum = BigDecimal.ZERO;
		for (LinkedList<Object> linha : this.data) {
			try {
				sum = sum.add((BigDecimal) linha.get(index));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sum;
	}

	public BigDecimal getColumnSum(String header) {
		Integer index = this.headers.indexOf(header);
		if (index < 0) {
			System.out.println("Header não encontrado: " + header);
			return null;
		}
		
		BigDecimal sum = BigDecimal.ZERO;
		for (LinkedList<Object> linha : this.data) {
			try {
				sum = sum.add((BigDecimal) linha.get(index));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sum;
	}

	public void addColumn(int row, String string) {
		data.get(row).add(string);
	}

}

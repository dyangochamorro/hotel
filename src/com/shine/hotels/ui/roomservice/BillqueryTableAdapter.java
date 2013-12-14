package com.shine.hotels.ui.roomservice;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BillqueryTableAdapter extends BaseAdapter {
	private Context context;
	private List<TableRow> table;
	public BillqueryTableAdapter(Context context, List<TableRow> table) {
		this.context = context;
		this.table = table;
	}
	@Override
	public int getCount() {
		return table.size();
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	public TableRow getItem(int position) {
		return table.get(position);
	}
	public View getView(int position, View convertView, ViewGroup parent) {
		TableRow tableRow = table.get(position);
		if (null == tableRow)
			return null ;
		return new TableRowView(this.context, tableRow);
	}
	/**
	 * TableRowView 实现表格行的样式
	 * @author hellogv
	 */
	class TableRowView extends LinearLayout {
		public TableRowView(Context context, TableRow tableRow) {
			super(context);
			
			this.setOrientation(LinearLayout.HORIZONTAL);
			for (int i = 0; i < tableRow.getSize(); i++) {//逐个格单元添加到行
				TableCell tableCell = tableRow.getCellValue(i);
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(tableCell.width, tableCell.height);//按照格单元指定的大小设置空间
				layoutParams.setMargins(0, 0, 0, 0);//预留空隙制造边框
				
				TextView textCell = new TextView(context);
				if (1==i) {
					textCell.setGravity(Gravity.CENTER_VERTICAL);
				} else {
					textCell.setGravity(Gravity.CENTER);
				}
				
				 if(i==3) {
					textCell.setTextColor(Color.RED) ;
				} else {
					textCell.setTextColor(Color.BLACK) ;
				}
				textCell.setText(String.valueOf(tableCell.value));
				addView(textCell, layoutParams);
			}
//			this.setBackgroundColor(Color.WHITE);//背景白色，利用空隙来实现边框
		}
	}
	/**
	 * TableRow 实现表格的行
	 * @author hellogv
	 */
	static public class TableRow {
		private TableCell[] cell;
		public TableRow(TableCell[] cell) {
			this.cell = cell;
		}
		public int getSize() {
			return cell.length;
		}
		public TableCell getCellValue(int index) {
			if (index >= cell.length)
				return null;
			return cell[index];
		}
	}
	/**
	 * TableCell 实现表格的格单元
	 * @author hellogv
	 */
	static public class TableCell {
		static public final int STRING = 0;
		static public final int IMAGE = 1;
		public Object value;
		public int width;
		public int height;
		private int type;
		public TableCell(Object value, int width, int height, int type) {
			this.value = value;
			this.width = width;
			this.height = height;
			this.type = type;
		}
	}
}

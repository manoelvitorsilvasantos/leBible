package br.app.mvictor;
import android.view.*;
import android.content.*;
import android.widget.*;
import br.app.mvictor.modelo.*;
import java.util.*;
import androidx.annotation.*;
import android.support.v7.widget.*;
import android.support.annotation.*;

public class ReclyclerViewAdapter extends RecyclerView.Adapter<ReclyclerViewAdapter.ViewHolder>
{
	private String[] mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

	// data is passed into the constructor
	
    ReclyclerViewAdapter(Context context, String[] data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }
	
	

    // inflates the cell layout from xml when needed
    @Override
    @NonNull 
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.celulas, parent, false);
        return new ViewHolder(view);
    }
	
	

		// binds the data to the TextView in each cell
		@Override
		public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
			holder.myTextView.setText(mData[position]);
		}

		// total number of cells
		@Override
		public int getItemCount() {
			return mData.length;
		}


		// stores and recycles views as they are scrolled off screen
		public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
			TextView myTextView;

			ViewHolder(View itemView) {
				super(itemView);
				myTextView = itemView.findViewById(R.id.info_text);
				itemView.setOnClickListener(this);
			}

			@Override
			public void onClick(View view) {
				if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
			}
		}

		// convenience method for getting data at click position
		
		String getItem(int id) {
			return mData[id];
		}
		
		

		// allows clicks events to be caught
		void setClickListener(ItemClickListener itemClickListener) {
			this.mClickListener = itemClickListener;
		}

		// parent activity will implement this method to respond to click events
		public interface ItemClickListener {
			void onItemClick(View view, int position);
		}
	
	
}

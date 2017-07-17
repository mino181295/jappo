package it.unibo.matteo.jappo.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.scalified.fab.ActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import it.unibo.matteo.jappo.Activity.MainActivity;
import it.unibo.matteo.jappo.Adapter.FavoritesAdapter;
import it.unibo.matteo.jappo.Adapter.OrderAdapter;
import it.unibo.matteo.jappo.Model.DataModel;
import it.unibo.matteo.jappo.Model.Item;
import it.unibo.matteo.jappo.Model.Order;
import it.unibo.matteo.jappo.Model.Restaurant;
import it.unibo.matteo.jappo.Model.Type;
import it.unibo.matteo.jappo.R;

import static android.R.attr.breadCrumbShortTitle;
import static android.R.attr.order;
import static android.R.string.cancel;
import static android.content.ContentValues.TAG;
import static android.os.Build.VERSION_CODES.M;
import static it.unibo.matteo.jappo.Fragment.FavoritesFragment.favorites;
import static it.unibo.matteo.jappo.Fragment.NewOrderFragment.restourants;

public class OrderFragment extends Fragment {

    View mView;
    ActionButton closeButton;
    ActionButton addItemButton;

    static Order order;
    OrderAdapter orderAdapter;
    static ArrayList<Item> orderedItems;

    private OnOrderInteractionListener mListener;

    public OrderFragment() {
    }

    public static OrderFragment newInstance(Order o) {
        OrderFragment fragment = new OrderFragment();
        order = o;
        orderedItems = o.getOrderedItems();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnOrderInteractionListener) {
            mListener = (OnOrderInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnNewOrderInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_order, container, false);

        closeButton = (ActionButton) mView.findViewById(R.id.close_order_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCloseDialog();
            }
        });
        addItemButton = (ActionButton) mView.findViewById(R.id.add_item_button);
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDialog();
            }
        });

        ListView orderList = (ListView) mView.findViewById(R.id.order_list);
        orderAdapter = new OrderAdapter(getContext(), R.layout.order_item, orderedItems);
        orderList.setAdapter(orderAdapter);

        orderList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        orderList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                showDeleteDialog(i, orderAdapter);
                return false;
            }
        });

        TextView title = (TextView) mView.findViewById(R.id.title_order);
        title.setText("Ordine al ristorante " + order.getRestourant() );

        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                closeButton.show();
                addItemButton.show();
            }
        }, 300);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            Random r = new Random();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    addItemButton.show();
                }
            }, 100+r.nextInt(400));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    closeButton.show();
                }
            }, 100+r.nextInt(400));
        }
        else {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    addItemButton.hide();
                }
            });
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    closeButton.hide();
                }
            });
        }
    }

    private void showAddDialog(){
        final AlertDialog alertDialog;

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.add_item, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogView)
               .setCancelable(false);

        final CircleImageView typeImage = (CircleImageView) dialogView.findViewById(R.id.item_image);
        final TextView mNumber = (TextView) dialogView.findViewById(R.id.add_item_number_text);
        final Spinner mSpinner = (Spinner) dialogView.findViewById(R.id.add_type_spinner);
        final TextView mNameView = (TextView) dialogView.findViewById(R.id.add_item_name_text);
        mNameView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String actualText = charSequence.toString().toLowerCase();
                String[] wordsList = actualText.split(" ");
                List<Type> typeList = Type.getList();
                for (Type t : typeList) {
                    String typeName = t.getName().toLowerCase();
                    for (int index = 0; index<wordsList.length; index++){
                        if (typeName.contains(wordsList[index])){
                            int current = typeList.indexOf(t);
                            mSpinner.setSelection(current);
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
        final List<Type> typeList = Type.getList();

        ArrayAdapter<Type> typeAdapter = new ArrayAdapter<Type>(getContext(), R.layout.type_item,
                R.id.type_name_text, typeList);
        typeAdapter.setDropDownViewResource(R.layout.type_item);
        mSpinner.setAdapter(typeAdapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                typeImage.setImageResource(typeList.get(i).getImage());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mSpinner.setSelection(0);

        alertDialog = builder.create();
        Button mCancel = (Button) dialogView.findViewById(R.id.button_cancel);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        Button mAdd = (Button) dialogView.findViewById(R.id.button_add);
        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mNumber.getText().toString().equals("")){

                    String name = mNameView.getText().toString();
                    int number = Integer.parseInt(mNumber.getText().toString());
                    Type selectedItem = (Type) mSpinner.getSelectedItem();
                    Item newItem = new Item(name, number, selectedItem, order.getRestourant());
                    newItem.setTime(Item.getCurrentTime());

                    orderedItems.add(newItem);
                    orderAdapter.notifyDataSetChanged();

                    alertDialog.dismiss();
                } else {
                    mNumber.setError("Inserire numero");
                }
            }
        });
        alertDialog.show();
    }

    private void showDeleteDialog(int i, final OrderAdapter orderAdapter) {
        final int index = i;

        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(getContext());

        final CharSequence [] mTitles = {"Elimina elemento","Modifica elemento", "Segna come arrivato"};

        builder.setItems(mTitles, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0:
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());;
                        builder.setTitle("Elimina")
                                .setMessage("Vuoi veramente eliminare "+ orderedItems.get(i).getName() + " dal corrente ordine?")
                                .setPositiveButton("Elimina", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        orderedItems.remove(index);
                                        orderAdapter.notifyDataSetChanged();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .setCancelable(true)
                                .show();

                        break;
                    case 2:
                        break;
                }
            }
        });
        builder.create().show();
    }

    private void showCloseDialog() {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Chiudi ordine")
                .setMessage("Vuoi veramente chiudere l'ordine di "+ order.getRestourant().getName() + "?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        onCloseOrder();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setCancelable(true)
                .show();
    }

    public void onCloseOrder() {
        if (mListener != null) {
            mListener.onOrderInteraction();
        }
    }

    public interface OnOrderInteractionListener {
        void onOrderInteraction();
    }


}
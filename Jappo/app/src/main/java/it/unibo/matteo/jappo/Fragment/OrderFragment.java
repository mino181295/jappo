package it.unibo.matteo.jappo.Fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import com.scalified.fab.ActionButton;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import de.hdodenhof.circleimageview.CircleImageView;
import it.unibo.matteo.jappo.Activity.MainActivity;
import it.unibo.matteo.jappo.Adapter.OrderAdapter;
import it.unibo.matteo.jappo.Model.Item;
import it.unibo.matteo.jappo.Model.Order;
import it.unibo.matteo.jappo.Model.Type;
import it.unibo.matteo.jappo.R;
import it.unibo.matteo.jappo.Utils.AlarmNotificationReceiver;
import it.unibo.matteo.jappo.Utils.MediaHelper;
import it.unibo.matteo.jappo.Utils.VibratorManager;

import static android.content.Context.ALARM_SERVICE;

/**
 * Activity that manages an {@link Order} inside the {@link MainActivity}
 */
public class OrderFragment extends Fragment {

    private ActionButton closeButton;
    private ActionButton addItemButton;
    private TextView mEmptyLabel;

    private static ArrayList<Item> orderedItems;
    private static Order order;

    private OrderAdapter orderAdapter;
    private ListView orderList;

    boolean hasToNotificate = true;

    private OnOrderInteractionListener mListener;

    public OrderFragment() {}

    public static OrderFragment newInstance(Order o) {
        OrderFragment fragment = new OrderFragment();
        order = o;
        orderedItems = o.getOrdered();
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
        mListener = null;
        super.onDetach();
    }

    @Override
    public void onStop() {
        if (orderedItems.size() > 0 && hasToNotificate){
            startAlarm();
        }
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        stopAlarm();
    }

    /**
     * Starts the Alarm.
     * The {@link AlarmManager} triggers a {@link android.content.BroadcastReceiver} that pops in a Custom
     * {@link android.app.Notification}
     */
    private void startAlarm() {
        AlarmManager manager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext() , AlarmNotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, 0);
        int waitTime = 1000 * 60 * 2;
        manager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + waitTime, pendingIntent);
    }

    /**
     * Stop the {@link AlarmManager} from popping a {@link android.app.Notification}
     */
    private void stopAlarm(){
        AlarmManager manager = (AlarmManager) getContext().getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(getContext(), AlarmNotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, 0);
        manager.cancel(pendingIntent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_order, container, false);

        /* View setup */
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

        mEmptyLabel = (TextView)mView.findViewById(R.id.order_empty_text);

        /* Order list setup with the Adapter */
        orderList = (ListView) mView.findViewById(R.id.order_list);
        orderAdapter = new OrderAdapter(getContext(), R.layout.order_item, orderedItems);
        orderList.setAdapter(orderAdapter);

        orderList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                VibratorManager.makeBuzz(getContext(), VibratorManager.SHORT);
                showOptionsDialog(i);
            }
        });

        TextView title = (TextView) mView.findViewById(R.id.title_order);
        title.setText("Ordine al ristorante " + order.getRestaurant() );

        refreshOrder();

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
        /* Pops in the floating buttons */
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

    /**
     * Shows the Add {@link Item} product to the list
     */
    private void showAddDialog(){
        final AlertDialog alertDialog;

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.add_item, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogView)
               .setCancelable(false);

        final CircleImageView typeImage = (CircleImageView) dialogView.findViewById(R.id.completed_image);
        final TextView mNumber = (TextView) dialogView.findViewById(R.id.add_item_number_text);
        final Spinner mSpinner = (Spinner) dialogView.findViewById(R.id.add_type_spinner);
        final TextView mNameView = (TextView) dialogView.findViewById(R.id.add_item_name_text);
        mNameView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Suggestion of the item
                String actualText = charSequence.toString().toLowerCase();
                String[] wordsList = actualText.split(" ");
                List<Type> typeList = Type.getList();
                for (Type t : typeList) {
                    String typeName = t.getName().toLowerCase();
                    for (String aWordsList : wordsList) {
                        if (typeName.contains(aWordsList)) {
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
        /* Adapter setup for the type Dropdown selection */
        ArrayAdapter<Type> typeAdapter = new ArrayAdapter<>(getContext(), R.layout.type_item,
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

        /* Listener setup for the add or cancel buttons */
        final String insertNumberText = getString(R.string.insert_number);
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
                    Item newItem = new Item(name, number, selectedItem, order.getRestaurant());
                    newItem.setTime(Item.getCurrentTime());

                    orderedItems.add(newItem);
                    refreshOrder();
                    alertDialog.dismiss();
                } else {
                    mNumber.setError(insertNumberText);
                }
            }
        });
        alertDialog.show();
    }

    /**
     * Shows the edit {@link android.app.Dialog} filled with the current information of the {@link Item}
     * @param position position of the {@link Item} to change
     */
    private void showEditDialog(final int position){
        final Item editItem = orderedItems.get(position);

        final AlertDialog alertDialog;
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.add_item, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogView)
                .setCancelable(false);

        /* View setup */
        final CircleImageView typeImage = (CircleImageView) dialogView.findViewById(R.id.completed_image);
        final TextView mNumber = (TextView) dialogView.findViewById(R.id.add_item_number_text);
        mNumber.setText(String.valueOf(editItem.getNumber()));
        final Spinner mSpinner = (Spinner) dialogView.findViewById(R.id.add_type_spinner);
        final TextView mNameView = (TextView) dialogView.findViewById(R.id.add_item_name_text);
        mNameView.setText(editItem.getName());
        mNameView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Suggestion of the item
                String actualText = charSequence.toString().toLowerCase();
                String[] wordsList = actualText.split(" ");
                List<Type> typeList = Type.getList();
                for (Type t : typeList) {
                    String typeName = t.getName().toLowerCase();
                    for (String aWordsList : wordsList) {
                        if (typeName.contains(aWordsList)) {
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

        /* Adapter of the type setup */
        ArrayAdapter<Type> typeAdapter = new ArrayAdapter<>(getContext(), R.layout.type_item,
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
        mSpinner.setSelection(editItem.getType().getNumber()-1);

        alertDialog = builder.create();
        final String insertNumberText = getString(R.string.insert_number);
        Button mCancel = (Button) dialogView.findViewById(R.id.button_cancel);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        Button mAdd = (Button) dialogView.findViewById(R.id.button_add);
        mAdd.setText(R.string.edit);
        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mNumber.getText().toString().equals("")){
                    String name = mNameView.getText().toString();
                    int number = Integer.parseInt(mNumber.getText().toString());
                    Type selectedItem = (Type) mSpinner.getSelectedItem();
                    Item editItem = new Item(name, number, selectedItem, order.getRestaurant());
                    editItem.setTime(new Date());

                    orderedItems.set(position, editItem);
                    refreshOrder();
                    alertDialog.dismiss();
                } else {
                    mNumber.setError(insertNumberText);
                }
            }
        });
        alertDialog.show();
    }

    /**
     * Shows an mutiple choice {@link AlertDialog} where you can choose the option.
     * @param i position of the target {@link Item}
     */
    private void showOptionsDialog(int i) {
        final int index = i;

        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(getContext());

        final CharSequence [] mTitles = { getString(R.string.delete_item),
                                          getString(R.string.edit_item),
                                          getString(R.string.mark_arrived)
                                        };

        builder.setItems(mTitles, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0:
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle(R.string.delete)
                                .setMessage(getString(R.string.delete_question) + " " +
                                        orderedItems.get(i).getName() + " "
                                        + getString(R.string.from_current_order))
                                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        orderedItems.remove(index);
                                        fadeOutView(orderList.getChildAt(index));
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                refreshOrder();
                                            }
                                        }, 400);
                                    }
                                })
                                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {}
                                })
                                .setCancelable(true)
                                .show();

                        break;
                    case 1:
                        showEditDialog(index);
                        break;
                    case 2:
                        MainActivity activity = (MainActivity)getContext();
                        CompletedFragment completedFragment = (CompletedFragment) activity.getViewerFragment(2);

                        order.setArrivedItem(orderedItems.get(index));
                        slideOutView(orderList.getChildAt(index));
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                refreshOrder();
                            }
                        }, 500);
                        completedFragment.refreshCompleted();
                        break;
                }
            }
        });
        builder.create().show();
    }

    /**
     * {@link AlertDialog} to confirm if you have to really close the order
     */
    private void showCloseDialog() {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.close_order)
                .setMessage(getString(R.string.close_question) +
                        " " + order.getRestaurant().getName() + "?")
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        onCloseOrder();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setCancelable(true)
                .show();
    }

    /**
     * Refresh the {@link ListView} with the correct visualization of a label if empty.
     */
    public void refreshOrder(){
        orderAdapter.notifyDataSetChanged();
        int completedNumber = orderedItems.size();
        if (completedNumber != 0){
            mEmptyLabel.setVisibility(View.INVISIBLE);
        } else {
            mEmptyLabel.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Close order function that finalize the current order and temp folders.
     */
    public void onCloseOrder() {
        if (mListener != null) {
            MediaHelper.deleteFolder(getContext());
            hasToNotificate = false;
            mListener.onOrderInteraction();
        }
    }

    /**
     * Slide out animation a {@link View} when it arrives
     * @param view
     */
    private void slideOutView(View view) {
        Animation slideOut = AnimationUtils.loadAnimation(view.getContext(), R.anim.slide_out_right);
        final View finalView = view;
        if (slideOut != null) {
            slideOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    finalView.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            view.startAnimation(slideOut);
        }
    }

    /**
     * Fade out a {@link View} when it is deleted
     * @param view
     */
    private void fadeOutView(View view) {
        Animation slideOut = AnimationUtils.loadAnimation(view.getContext(), R.anim.fade_out_animation);
        final View finalView = view;
        if (slideOut != null) {
            slideOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    finalView.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            view.startAnimation(slideOut);
        }
    }

    /**
     * Interface that permits the interaction with the attached {@link MainActivity}
     */
    public interface OnOrderInteractionListener {
        void onOrderInteraction();
    }


}
package com.uottawa.plscuddleme;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yuhan on 11/19/2017.
 */

public class AddResourcesActivity extends Fragment implements View.OnClickListener {
    private static final String TAG = "AddResourcesActivity";
    ListView listViewResources;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    Button buttonAddNewResource;
    Button buttonCancel;
    View dialogView;
    Spinner choreSpinner;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("AddResourcesActivity");
        buttonAddNewResource = (Button) getView().findViewById(R.id.buttonAddResource);
        buttonCancel = (Button) getView().findViewById(R.id.buttonCancel);
        String[] shoppingList = {"Pencil", "Eraser", "Rocket League", "Rocket League Controller", "Dog Food", "Dog"};
        listViewResources = (ListView) getView().findViewById(R.id.list_shopping);

        ResourceCustomAdapter adapter = new ResourceCustomAdapter(getContext(), shoppingList);
        listViewResources.setAdapter(adapter);

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dialogView = inflater.inflate(R.layout.add_resource_dialog, null);

        choreSpinner = (Spinner)getView().findViewById(R.id.spinnerAssignedChore);
        buttonAddNewResource.setOnClickListener(this);

        listViewResources.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int i, long l) {
                final int selectedRow = i;
                DatabaseReference databaseProducts;
                databaseProducts = FirebaseDatabase.getInstance().getReference().child("housechores");
                databaseProducts.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int counter = 0;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Housechore housechore = snapshot.getValue(Housechore.class);
                            if (counter == selectedRow) {
                                showEditResourceDialog(housechore.getID());
                            }
                            counter = counter + 1;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
                return true;
            }
        });
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.nav_resources, container, false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonAddResource:
                showAddResourceDialog();
                break;
            case R.id.buttonCancel:
                openDrawer();
        }

    }

    private void showAddResourceDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.add_resource_dialog, null);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setTitle("Add a Resource");
        final AlertDialog b = dialogBuilder.create();
        b.show();

        final Button buttonCancel = (Button) dialogView.findViewById(R.id.buttonCancel);
        final Button buttonAddResource = (Button) dialogView.findViewById(R.id.buttonAddResource);

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
            }
        });

        buttonAddResource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
                Spinner choreSpinner = (Spinner) dialogView.findViewById(R.id.spinnerAssignedChore);
                EditText editTextResourceName = (EditText) dialogView.findViewById(R.id.editTextResourceName);
                String resourceName = editTextResourceName.getText().toString().trim();
                addResource(resourceName, choreSpinner.getSelectedItem().toString());
            }
        });


        DatabaseReference databaseHousechores;
        databaseHousechores = FirebaseDatabase.getInstance().getReference().child("housechores");
        databaseHousechores.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> chores = new ArrayList<String>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Housechore chore = snapshot.getValue(Housechore.class);
                    String choreName = chore.getHousechoreName();
                    chores.add(choreName);
                }
                Spinner choreSpinner = (Spinner) dialogView.findViewById(R.id.spinnerAssignedChore);
                ArrayAdapter<String> choresAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, chores);
                choresAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                choreSpinner.setAdapter(choresAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    private void showEditResourceDialog(final String id) {
        //TODO Update Resource
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.edit_resource_dialog, null);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setTitle("Edit a Resource");
        final AlertDialog b = dialogBuilder.create();
        b.show();

        final Button buttonCancel = (Button) dialogView.findViewById(R.id.buttonCancel);
        final Button buttonEditResource = (Button) dialogView.findViewById(R.id.buttonEditResource);

        DatabaseReference databaseHousechores;
        databaseHousechores = FirebaseDatabase.getInstance().getReference().child("housechores");
        databaseHousechores.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> chores = new ArrayList<String>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Housechore chore = snapshot.getValue(Housechore.class);
                    String choreName = chore.getHousechoreName();
                    chores.add(choreName);
                }
                Spinner choreSpinner = (Spinner) dialogView.findViewById(R.id.spinnerNewAllocatedChore);
                ArrayAdapter<String> choresAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, chores);
                choresAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                choreSpinner.setAdapter(choresAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
            }
        });

        buttonEditResource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateResource("", "", "");
                b.dismiss();
            }
        });

    }

    private void updateResource(String id, String previousResource, String newResource) {
//        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("housechores").child(id).child("resources");
//        dR.setValue(newResource);
        Toast.makeText(getActivity(), "Resource Updated", Toast.LENGTH_LONG).show();
    }


    //TODO 1 add resource to DB when clicked on add button. Two more things to do when adding to db
    //TODO 2 make sure that there is a list of resources in the housechore (I have already added the list item to the Housechore.class)
    //TODO 3 make sure the new resource is being appended there

    private void addResource(String resourceName, String allocatedChore) {
        //add in db
        Log.v(TAG,"**************");
        Log.v(TAG,"resourceName" +resourceName);
        Log.v(TAG,"allocatedChore" + allocatedChore);
        Log.v(TAG,"**************");

    }

    private void openDrawer() {
        startActivity(new Intent(getContext(), DrawerActivity.class));
    }
}

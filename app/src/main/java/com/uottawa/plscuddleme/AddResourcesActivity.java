package com.uottawa.plscuddleme;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
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

/**
 * This activity displays all the list view of resources
 */
public class AddResourcesActivity extends Fragment implements View.OnClickListener {
    private static final String TAG = "AddResourcesActivity";
    ListView listViewResources;
    Button buttonAddNewResource;
    Button buttonCancel;
    View dialogView;
    Spinner choreSpinner;
    ArrayAdapter<String> resourcesAdapter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Resources List");

        displayAllResources();
        buttonAddNewResource = (Button) getView().findViewById(R.id.buttonAddResource);
        buttonCancel = (Button) getView().findViewById(R.id.buttonCancel);
        listViewResources = (ListView) getView().findViewById(R.id.list_resources);

        //Inflater needed to get an id that does not belong to the current view and populates the
        //spinner of chores
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dialogView = inflater.inflate(R.layout.add_resource_dialog, null);

        choreSpinner = (Spinner) getView().findViewById(R.id.spinnerAssignedChore);
        buttonAddNewResource.setOnClickListener(this);

        //if long click on the chore item, an update resource dialog will show up
        listViewResources.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int i, long l) {
                final int selectedRow = i;
                DatabaseReference databaseResources;
                databaseResources = FirebaseDatabase.getInstance().getReference().child("resources");
                databaseResources.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int counter = 0;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Resource resource = snapshot.getValue(Resource.class);
                            if (counter == selectedRow) {
                                showEditResourceDialog(resource.getID());
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
               displayAllResources();
               //TODO check if this will break
        }

    }

    /**
     * Function shows add resource dialog and sets its views and gets all references from it with an inflater
     * Populates the spinner of allocated housechore with what's in the current db
     */
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

        //sets the onclick when adding a resource and calls method addResource to add in db
        buttonAddResource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Spinner choreSpinner = (Spinner) dialogView.findViewById(R.id.spinnerAssignedChore);
                EditText editTextResourceName = (EditText) dialogView.findViewById(R.id.editTextResourceName);
                String resourceName = editTextResourceName.getText().toString().trim();
                addResource(resourceName, choreSpinner.getSelectedItem().toString());
                b.dismiss();
                displayAllResources();
            }
        });


        //Populates the spinner with all the housechores in the db
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

                //Set spinner possible fields
                Spinner choreSpinner = (Spinner) dialogView.findViewById(R.id.spinnerAssignedChore);
                resourcesAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, chores);
                resourcesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                choreSpinner.setAdapter(resourcesAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    /**
     * This function displays a editResource dialog and gets buttons from the dialog view
     * When the mandatory fields are entered, it will update the chore value in the db
     * @param id = id of the resource in the db
     */
    private void showEditResourceDialog(final String id) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());

        //gets the layout id with inflater and set it to view
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.edit_resource_dialog, null);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setTitle("Edit a Resource");
        final AlertDialog b = dialogBuilder.create();
        b.show();

        //Get all UI references from the dialogVIew
        final Button buttonCancel = (Button) dialogView.findViewById(R.id.buttonCancel);
        final Button buttonEditResource = (Button) dialogView.findViewById(R.id.buttonEditResource);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDelete);

        //This gets all the current chores in the database and populates the spinner in the
        //allocated chore spinner in the dialogView
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

                //here gets the dialogView and sets the list of chores in the choreAdapter
                Spinner choreSpinner = (Spinner) dialogView.findViewById(R.id.spinnerNewAllocatedChore);
                ArrayAdapter<String> choresAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, chores);
                choresAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                choreSpinner.setAdapter(choresAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        final Spinner choreSpinner = (Spinner) dialogView.findViewById(R.id.spinnerNewAllocatedChore);
        final EditText editTextResourceName = (EditText) dialogView.findViewById(R.id.editTextResourceName);

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteResource(id);
                b.dismiss();
                displayAllResources();
            }
        });

        buttonEditResource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String resourceName = editTextResourceName.getText().toString().trim();
                final String assignedChoreName = choreSpinner.getSelectedItem().toString();
                updateResource(id, assignedChoreName, resourceName);
                b.dismiss();
                displayAllResources();
            }
        });

    }

    /**
     * This function removes a resource based on the passed ID in the db
     * and makes a toast
     * @param id = ID of the reference in the db
     * @return
     */
    private boolean deleteResource(String id) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("resources").child(id);
        dR.removeValue();
        Toast.makeText(getActivity(), "Resource Deleted", Toast.LENGTH_LONG).show();
        return true;
    }

    /**
     * @param id = id of the chore
     * @param newHousechore = the new allocated housechore (name in string)
     * @param newResource = the new resource object (containing different values)
     */
    private void updateResource(String id, String newHousechore, String newResource) {
        DatabaseReference dRHousechore = FirebaseDatabase.getInstance().getReference("resources").child(id).child("housechore");
        DatabaseReference dRResource = FirebaseDatabase.getInstance().getReference("resources").child(id).child("resourceName");
        dRHousechore.setValue(newHousechore);
        dRResource.setValue(newResource);
        Toast.makeText(getActivity(), "Resource Updated", Toast.LENGTH_LONG).show();
    }

    /**
     *This function adds the resource with attributes in the dialog view if all fields are entered
     * @param resourceName = name of the resource
     * @param allocatedChore = The choreName assigning to
     */
    private void addResource(final String resourceName, final String allocatedChore) {
        final DatabaseReference databaseResources;
        databaseResources = FirebaseDatabase.getInstance().getReference("resources");

        if (TextUtils.isEmpty(resourceName)) {
            Toast.makeText(getActivity(),"Resource name cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }

        if (!TextUtils.isEmpty(resourceName)) {
            final String id = databaseResources.push().getKey();
            DatabaseReference databaseHousechores;
            databaseHousechores = FirebaseDatabase.getInstance().getReference("housechores");
            databaseHousechores.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Housechore housechore = postSnapshot.getValue(Housechore.class);

                        //This checks whether the allocatedChore is = to the housechore object in the db
                        //The spinner is already populated with the housechores currently in the db
                        //but checks again just to make sure
                        if (housechore.getHousechoreName().equals(allocatedChore)) {
                            String housechoreName = housechore.getHousechoreName();
                            Resource resource = new Resource(id, resourceName, housechoreName);
                            databaseResources.child(id).setValue(resource);
                            Toast.makeText(getActivity(), "Resource Added", Toast.LENGTH_LONG).show();
                        }
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }

    private void openDrawer() {
        startActivity(new Intent(getContext(), DrawerActivity.class));
    }

    public void displayAllResources() {
        DatabaseReference databaseResources;
        databaseResources = FirebaseDatabase.getInstance().getReference().child("resources");
        databaseResources.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                String[] resourcesList = new String[(int) dataSnapshot.getChildrenCount()];
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Resource resource = snapshot.getValue(Resource.class);
                    resourcesList[i] = resource.getResourceName();
                    i = i + 1;

                }
                ListView listView = (ListView) getView().findViewById(R.id.list_resources);
                ResourceCustomAdapter adapter = new ResourceCustomAdapter(getActivity(), resourcesList);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}

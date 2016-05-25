package org.wordpress.android.ui.people;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import org.wordpress.android.R;
import org.wordpress.android.datasets.PeopleTable;
import org.wordpress.android.models.Person;

import java.util.List;

public class PeopleListFragment extends ListFragment implements OnItemClickListener {
    private static String ARG_LOCAL_TABLE_BLOG_ID = "local_table_blog_id";

    private int mLocalTableBlogID;
    private OnPersonSelectedListener mListener;

    public static PeopleListFragment newInstance(int localTableBlogID) {
        PeopleListFragment peopleListFragment = new PeopleListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_LOCAL_TABLE_BLOG_ID, localTableBlogID);
        peopleListFragment.setArguments(bundle);
        return peopleListFragment;
    }

    public void setOnPersonSelectedListener(OnPersonSelectedListener listener) {
        mListener = listener;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.people_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        return inflater.inflate(R.layout.people_list_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mLocalTableBlogID = getArguments().getInt(ARG_LOCAL_TABLE_BLOG_ID);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        refreshPeopleList();
    }

    public void refreshPeopleList() {
        if (!isAdded()) return;

        List<Person> peopleList = PeopleTable.getPeople(mLocalTableBlogID);

        PeopleAdapter peopleAdapter = (PeopleAdapter) getListAdapter();
        if (peopleAdapter == null) {
            peopleAdapter = new PeopleAdapter(getActivity(), peopleList);
            setListAdapter(peopleAdapter);
        } else {
            peopleAdapter.setPeopleList(peopleList);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mListener != null) {
            Person person = (Person) parent.getItemAtPosition(position);
            mListener.onPersonSelected(person);
        }
    }

    // Container Activity must implement this interface
    public interface OnPersonSelectedListener {
        void onPersonSelected(Person person);
    }
}

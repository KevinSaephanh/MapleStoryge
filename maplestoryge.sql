-------------------- ROLE/TCL OPERATIONS ---------------------

create role maple_manager with password 'maplestoryge7';

alter role "maple_manager" with login;

grant all privileges on table users to maple_manager;

grant all privileges on table maplestoryges to maple_manager;

grant all privileges on table users_maplestoryges to maple_manager;

grant usage, select on all sequences in schema public TO maple_manager;

------------------------ TABLES -----------------------------

create table if not exists users (
	user_id SERIAL primary key,
	user_name VARCHAR(25) unique not null,
	pass_word VARCHAR(50) not null
);

create table if not exists maplestoryges (
	maplestoryge_id SERIAL primary key,
	storage_type VARCHAR(15) not null,
	title VARCHAR(25) unique not null,
	mesos NUMERIC(15, 2) default 0 check (mesos >= 0) -- currency
);

create table if not exists users_maplestoryges (
	user_id SERIAL references users (user_id),
	maplestoryge_id SERIAL references maplestoryges (maplestoryge_id),
	constraint user_maplestoryge_pk primary key (user_id, maplestoryge_id)
);

------------------------ FUNCTIONS ----------------------

create or replace function deposit(id integer, amount numeric)
returns numeric(15, 2) as $$
begin
	update maplestoryges
	set mesos = mesos + amount
	where maplestoryge_id = id;
	return (select mesos from maplestoryges where maplestoryge_id = id);
end;
$$ language plpgsql;

create or replace function withdraw(id integer, amount numeric)
returns numeric(15, 2) as $$
begin
	update maplestoryges
	set mesos = mesos - amount
	where maplestoryge_id = id;
	return (select mesos from maplestoryges where maplestoryge_id = id);
end;
$$ language plpgsql;

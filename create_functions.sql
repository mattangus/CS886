delimiter //

select 'add_observation' as '';
DROP PROCEDURE IF EXISTS gamedata.add_observation//
CREATE PROCEDURE gamedata.add_observation(
	IN i_profile integer,
	IN i_strategy integer,
	IN i_o_agents_profile integer,
	IN i_num_players integer,
	IN i_payoff double)
BEGIN
	insert into symmetric_aggs 
		(profile_id, strategy_id, o_agents_profile_id, num_players, payoff)
	values 
		(i_profile, i_strategy, i_o_agents_profile, i_num_players, i_payoff);

END//


select 'add_profile' as '';
DROP FUNCTION IF EXISTS gamedata.add_profile//
CREATE FUNCTION gamedata.add_profile(
	i_assignment varchar(500),
	i_num_strategies integer) RETURNS integer
BEGIN
	DECLARE l_id integer default null;
	
	set l_id := (select profile_id
				from profiles
				where assignment = i_assignment
				and   num_strategies = i_num_strategies);

	if l_id is null then
		insert into profiles (assignment, num_strategies)
		VALUES (i_assignment, i_num_strategies);
		
		select LAST_INSERT_ID() into l_id;
	end if;

	return l_id;
END//



select 'add_o_agent_profile' as '';
DROP FUNCTION IF EXISTS gamedata.add_o_agent_profile//
CREATE FUNCTION gamedata.add_o_agent_profile(
	i_o_agents_profile_id integer,
	i_profile_encoding integer) RETURNS integer
BEGIN
	DECLARE l_id integer default null;

	if o_agents_profile_id is null then
		insert into o_agents_profile 
			(profile_encoding)
		values
			(i_profile_encoding);

		select LAST_INSERT_ID() into l_id;
	else
		insert into o_agents_profile (
			o_agents_profile_id,
			profile_encoding)
		values (
			i_o_agents_profile_id,
			i_profile_encoding);

		set l_id := o_agents_profile_id;
	end if;

	return l_id;
END//


select 'add_strategy' as '';
DROP FUNCTION IF EXISTS gamedata.add_strategy//
CREATE FUNCTION gamedata.add_strategy(
	i_profile_encoding integer) RETURNS integer
BEGIN
	DECLARE l_id integer default null;
	
	set l_id := (select strategy_id
				from strategies
				where profile_encoding = i_profile_encoding);

	IF l_id is null then
		insert into strategies (profile_encoding) values (i_profile_encoding);
		
		select LAST_INSERT_ID() into l_id;
	end if;

	return l_id;
END//


select 'get_o_agent_profile_id' as '';
DROP FUNCTION IF EXISTS gamedata.get_o_agent_profile_id//
CREATE FUNCTION gamedata.get_o_agent_profile_id(
	i_profile_encoding_1 integer,
	i_profile_encoding_2 integer,
	i_profile_encoding_3 integer,
	i_profile_encoding_4 integer) RETURNS integer
BEGIN
	DECLARE l_id integer default null;

	IF i_profile_encoding_2 is null then
		set l_id := 
		(select oap.o_agents_profile_id
		from o_agents_profile oap
		group by oap.o_agents_profile_id
		having sum(oap.profile_encoding = i_profile_encoding_1) > 0
		and    sum(oap.profile_encoding not in (i_profile_encoding_1)) = 0);
	elseif i_profile_encoding_3 is null then 
		set l_id := 
		(select oap.o_agents_profile_id
		from o_agents_profile oap
		group by oap.o_agents_profile_id
		having sum(oap.profile_encoding = i_profile_encoding_1) > 0
		and    sum(oap.profile_encoding = i_profile_encoding_2) > 0
		and    sum(oap.profile_encoding not in ( i_profile_encoding_1,
												 i_profile_encoding_2)) = 0);
	elseif i_profile_encoding_4 is null then
		set l_id := 
		(select oap.o_agents_profile_id
		from o_agents_profile oap
		group by oap.o_agents_profile_id
		having sum(oap.profile_encoding = i_profile_encoding_1) > 0
		and    sum(oap.profile_encoding = i_profile_encoding_2) > 0
		and    sum(oap.profile_encoding = i_profile_encoding_3) > 0
		and    sum(oap.profile_encoding not in ( i_profile_encoding_1,
												 i_profile_encoding_2,
												 i_profile_encoding_3)) = 0);
	else
		set l_id := 
		(select oap.o_agents_profile_id
		from o_agents_profile oap
		group by oap.o_agents_profile_id
		having sum(oap.profile_encoding = i_profile_encoding_1) > 0
		and    sum(oap.profile_encoding = i_profile_encoding_2) > 0
		and    sum(oap.profile_encoding = i_profile_encoding_3) > 0
		and    sum(oap.profile_encoding = i_profile_encoding_4) > 0
		and    sum(oap.profile_encoding not in ( i_profile_encoding_1,
												 i_profile_encoding_2,
												 i_profile_encoding_3,
												 i_profile_encoding_4)) = 0);
	end if;

	return l_id;
END//

delimiter ;
delimiter //

CREATE OR REPLACE PROCEDURE add_observation(
	IN i_profile integer,
	IN i_strategy integer,
	IN i_o_agents_profile integer,
	IN i_num_players integer,
	IN i_payoff double)
BEGIN
	insert into symmetric_aggs (profile_id, strategy_id, o_agents_profile_id, num_players, payoff)
	values (i_profile, i_strategy, i_o_agents_profile, i_num_players, i_payoff);

END//

CREATE FUNCTION add_profile(
	IN i_assignment varchar(255),
	IN i_num_strategies integer) RETURNS integer
BEGIN
	DECLARE l_id integer;
	
	insert into profiles (assignment, num_strategies)
	VALUES (i_assignment, i_num_strategies);
	
	select LAST_INSERT_ID() into l_id;
	return l_id;
END//

CREATE OR REPLACE PROCEDURE add_o_agent_profile(
	IN i_o_agents_profile_id integer,
	IN i_evasiveSpeedChange double,
	IN i_evasiveAngleChange double,
	IN i_evasiveRelativeTimeThresh double,
	IN i_desiredSpeed double,
	IN i_accelerationTime double,
	IN i_privateSphere double,
	IN i_visualRange double,
	IN i_fieldOfView double,
	IN i_repulsiveStrengthCoefA double,
	IN i_repulsiveStrengthCoefB double,
	IN i_crosswalkPullStrengthCoefA double,
	IN i_crosswalkPullStrengthCoefB double,
	IN i_crosswalkPushStrengthCoefA double,
	IN i_crosswalkPushStrengthCoefB double)
BEGIN
	insert into o_agents_profile (
		o_agents_profile_id,
		evasiveSpeedChange,
		evasiveAngleChange,
		evasiveRelativeTimeThresh,
		desiredSpeed,
		accelerationTime,
		privateSphere,
		visualRange,
		fieldOfView,
		repulsiveStrengthCoefA,
		repulsiveStrengthCoefB,
		crosswalkPullStrengthCoefA,
		crosswalkPullStrengthCoefB,
		crosswalkPushStrengthCoefA,
		crosswalkPushStrengthCoefB)
	values (
		i_o_agents_profile_id,
		i_evasiveSpeedChange,
		i_evasiveAngleChange,
		i_evasiveRelativeTimeThresh,
		i_desiredSpeed,
		i_accelerationTime,
		i_privateSphere,
		i_visualRange,
		i_fieldOfView,
		i_repulsiveStrengthCoefA,
		i_repulsiveStrengthCoefB,
		i_crosswalkPullStrengthCoefA,
		i_crosswalkPullStrengthCoefB,
		i_crosswalkPushStrengthCoefA,
		i_crosswalkPushStrengthCoefB);
END//

CREATE FUNCTION add_strategy(
	IN i_evasiveSpeedChange double,
	IN i_evasiveAngleChange double,
	IN i_evasiveRelativeTimeThresh double,
	IN i_desiredSpeed double,
	IN i_accelerationTime double,
	IN i_privateSphere double,
	IN i_visualRange double,
	IN i_fieldOfView double,
	IN i_repulsiveStrengthCoefA double,
	IN i_repulsiveStrengthCoefB double,
	IN i_crosswalkPullStrengthCoefA double,
	IN i_crosswalkPullStrengthCoefB double,
	IN i_crosswalkPushStrengthCoefA double,
	IN i_crosswalkPushStrengthCoefB double) RETURNS integer
BEGIN
	DECLARE l_id integer;
	
	insert into strategies (
		evasiveSpeedChange,
		evasiveAngleChange,
		evasiveRelativeTimeThresh,
		desiredSpeed,
		accelerationTime,
		privateSphere,
		visualRange,
		fieldOfView,
		repulsiveStrengthCoefA,
		repulsiveStrengthCoefB,
		crosswalkPullStrengthCoefA,
		crosswalkPullStrengthCoefB,
		crosswalkPushStrengthCoefA,
		crosswalkPushStrengthCoefB)
	values (
		i_evasiveSpeedChange,
		i_evasiveAngleChange,
		i_evasiveRelativeTimeThresh,
		i_desiredSpeed,
		i_accelerationTime,
		i_privateSphere,
		i_visualRange,
		i_fieldOfView,
		i_repulsiveStrengthCoefA,
		i_repulsiveStrengthCoefB,
		i_crosswalkPullStrengthCoefA,
		i_crosswalkPullStrengthCoefB,
		i_crosswalkPushStrengthCoefA,
		i_crosswalkPushStrengthCoefB);
	
	select LAST_INSERT_ID() into l_id;
	return l_id;
END//

delimiter ;
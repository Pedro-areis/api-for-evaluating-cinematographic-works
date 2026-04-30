create table scores (
    id UUID primary key default gen_random_uuid(),
    work_id UUID not null references works(id) on delete cascade,
    user_id UUID not null references users(id) on delete cascade,
    score numeric(3, 2) not null check (score >= 0 and score <= 10),
    created_at timestamp default current_timestamp,

    unique (work_id, user_id)
);

create or replace function update_work_average_score()
       returns trigger as $$
       begin

       update works
       set score = (
           select coalesce(round(avg(score), 1), 0)
           from scores
           where work_id = coalesce(new.work_id, old.work_id)
           )
       where id = coalesce(new.work_id, old.work_id);

       return new;
       end;
       $$ language plpgsql;

create trigger trigger_update_work_score
    after insert or update or delete on scores
    for each row
        execute function update_work_average_score();
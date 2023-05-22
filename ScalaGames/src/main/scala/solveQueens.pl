:- use_module(library(clpfd)).
sub_list(Xs, Ys) :- append(As, XsBs, Ys), append(Xs, Bs, XsBs).
n_queens(Qs) :- 
	length(Qs, 8),
	sub_list([1], Qs),
	sub_list([2], Qs),
	sub_list([3], Qs),
	sub_list([4], Qs),
	sub_list([5], Qs),
	sub_list([6], Qs),
	sub_list([7], Qs),
	sub_list([8], Qs),
	safe_queens(Qs).

safe_queens([]).

safe_queens([Q|Qs]) :- 
	safe_queens_(Qs, Q, 1), 
	safe_queens(Qs).

safe_queens_([], _, _). 
safe_queens_([Q|Qs], Q0, D0) :-
	Q #\= Q0, 
	abs(Q0 - Q) #\= D0, 
	D #= D0 + 1, 
	safe_queens_(Qs, Q0, D).

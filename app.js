document.addEventListener('DOMContentLoaded', () => {
    // id's do html
    const gamesList = document.getElementById('games-list');
    const addGameBtn = document.getElementById('add-game-btn');
    const popup = document.getElementById('add-game-popup');
    const closePopup = document.getElementById('close-popup');
    const addGameForm = document.getElementById('add-game-form');

    // api /game
    const apiGamesUrl = 'http://localhost:8080/game';

    // função para fazer solicitação na api
    const fetchData = (url) => {
        return fetch(url)
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.json();
            })
            .catch(error => {
                console.error('Erro ao buscar os dados:', error);
                throw new Error('Erro ao buscar os dados da API.');
            });
    };

    // retornando a lista de jogos
    const fetchGames = () => {
        return fetchData(apiGamesUrl);
    };

    // carregar a lista de jogos
    const loadGames = () => {
        fetchGames()
            .then(gamesData => {
                if (gamesData.length === 0) {
                    gamesList.innerHTML = '<li>Não há jogos disponíveis.</li>';
                } else {
                    gamesData.forEach(game => {
                        renderGame(game);
                    });
                }
            })
            .catch(error => {
                console.error('Erro ao buscar os dados:', error.message);
                gamesList.innerHTML = '<li>Erro ao carregar os jogos.</li>';
            });
    };

    // transformando o jogo em itens da lista html
    const renderGame = (game) => {
        const listItem = document.createElement('li');
        listItem.textContent = `Nome: ${game.title} - Data de lançamento: ${game.launchDate} - Preço: ${game.price} - Gênero: ${game.genres.map(genre => genre.genreName).join(', ')}`;
        const deleteButton = document.createElement('button');
        deleteButton.textContent = 'Remover';
        deleteButton.onclick = () => deleteGame(game.id, listItem);
        listItem.appendChild(deleteButton);
        gamesList.appendChild(listItem);
    };

    // deletar um jogo
    const deleteGame = (id, listItem) => {
        return fetch(`${apiGamesUrl}/${id}`, {
            method: 'DELETE',
        })
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            listItem.remove();
        })
        .catch(error => {
            console.error('Erro ao remover o jogo:', error.message);
        });
    };

    // carregando a lista de jogos
    loadGames();

    // abertura e fechamento do popup
    addGameBtn.onclick = () => {
        popup.style.display = 'block';
    };

    closePopup.onclick = () => {
        popup.style.display = 'none';
    };

    window.onclick = (event) => {
        if (event.target === popup) {
            popup.style.display = 'none';
        }
    };

    // adicionar novo jogo
    const addGame = (game) => {
        return fetch(apiGamesUrl, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(game),
        })
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            console.log('Novo jogo adicionado:', data);
            renderGame(data);
        })
        .catch(error => {
            console.error('Erro ao adicionar o jogo:', error.message);
        });
    };

    // formulario 
    addGameForm.onsubmit = async (event) => {
        event.preventDefault();

        // formatando a launchdate
        const launchDate = new Date(addGameForm.launchDate.value).toISOString().slice(0, 10);

        // obtendo o valor do genero
        const selectedGenre = addGameForm.genre.value;

        // formatando o genero para o aceito na api
        const formattedGenre = [
            { genreName: selectedGenre }
        ];

        const newGame = {
            title: addGameForm.title.value,
            launchDate: launchDate, // Data de lançamento formatada
            price: parseFloat(addGameForm.price.value),
            developer: 1, // fixando o id do desenvolvedor 
            publisher: 1, // fixando o id do publisher
            genres: formattedGenre //launchdate formatado
        };

        // adiciona um jogo novo a lista chamando a função addGame
        try {
            await addGame(newGame);
            popup.style.display = 'none';
            location.reload();
        } catch (error) {
            console.error('Erro ao adicionar o jogo:', error.message);
        }
    };
});

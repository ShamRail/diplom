let host = "http://localhost:8080/"

async function GetProjectDescription() {
    let url = "project_description"
    let response = await fetch(url);
    if (response.ok){
        return await response.json()
    }
}

async function GetProjectDocById(id) {
    let url = host + "project_docs?id=" + id
    let response = await fetch(url);
    if (response.ok){
        return await response.json()
    }
}
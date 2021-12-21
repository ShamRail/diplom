let host = "http://localhost:8084/"

async function AuthGetFetch(url) {
    let pass = localStorage.getItem('user')
    return await fetch(url, {
        headers: {
            "Authorization": "Basic " + pass
        }
    }).then(resp => {
        if (resp.ok) {
            return resp
        } else {
            alert("Not authorized")
        }
    });
}

async function GetProjectDescriptionAll() {
    let url = "project_description/"
    let response = await fetch(host + url + "all");
    if (response.ok) {
        return await response.json()
    }
}

async function GetProjectDescriptionByProjectId(project_id) {
    let url = host + "project_description/filter?project_id=" + project_id
    let response = await fetch(url);
    if (response.ok) {
        return await response.json()
    }
}

async function GetProjectDocById(id) {
    let url = host + "project_docs?id=" + id
    let response = await fetch(url);
    if (response.ok) {
        return await response.json()
    }
}

async function GetProjectDocsByUserId(id) {
    let url = host + "/project_doc/user?userId=" + id
    return await AuthGetFetch(url);
}

async function GetUser(email, password) {
    let url = host + "users?email=" + email
    let pass = btoa(email + ":" + password)
    let response = await fetch(url, {
        headers: {
            "Authorization": "Basic " + pass
        }
    });
    return response
}

async function DeleteGetProjectDocs(intent) {
    let url = host + "delete_project_doc"
    let pass = localStorage.getItem('user')
    return await fetch(url, {
        headers: {
            "Authorization": "Basic " + pass
        },
        method: "DELETE",
        body: JSON.stringify(intent)
    })
}

async function CreateProject(intent){
    let url = host + "project_doc"
    let pass = localStorage.getItem('user')
    return await fetch(url, {
        headers: {
            "Authorization": "Basic " + pass
        },
        method: "POST",
        body: JSON.stringify(intent)
    })
}


async function GetAllConfiguration(){
    let response = AuthGetFetch(host + "configuration/all");
    if(response.ok){
        return await response.json();
    }
    //return AuthGetFetch(host + "configuration/all")
}

async function  GetStatusProject(id){
    return AuthGetFetch(host + "/status?id=" + id)
}

async function BuildProject(projectId){
    let obj = {
        id: projectId
    }
    let url = host + "build"
    let pass = localStorage.getItem('user')
    return await fetch(url, {
        headers: {
            "Authorization": "Basic " + pass
        },
        method: "POST",
        body: JSON.stringify(obj)
    })
}